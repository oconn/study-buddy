(ns async-server.assistant.actions
  (:require [async-server.assistant.state :as state]
            [async-server.assistant.responses :as responses]))

;; Helpers

(def ^:private action-intents {:main "assistant.intent.action.MAIN"
                               :text "assistant.intent.action.TEXT"})

(defn- build-assistant-response
  "Builds assistant http response payload"
  [conversation-token expect-response? inputs]
  {:conversation_token conversation-token
   :expect_user_response expect-response?
   :expected_inputs inputs})

(defn- get-raw-text
  [arguments]
  ((comp keyword
          :raw_text
          first) arguments))

;; Step Actions

(defn- get-deck
  [conversation-id arguments state]
  (let [deck-name (get-raw-text arguments)
        deck (state/get-deck (keyword deck-name))]
    (if deck
      (do
        (state/set-next-step conversation-id :ask-first-question)
        (state/set-next-state conversation-id (assoc state :selected-deck deck))
        (build-assistant-response conversation-id
                                  true
                                  [{:input_prompt {:initial_prompts [{:ssml (responses/deck-found (name deck-name))}]
                                                   :no_input_prompts [{:ssml responses/deck-found-re-prompt-one}]}
                                    :possible_intents [{:intent (:text action-intents)}]}]))
      (build-assistant-response conversation-id
                                true
                                [{:input_prompt {:initial_prompts [{:ssml (responses/deck-not-found (name deck-name))}]
                                                 :no_input_prompts [{:ssml responses/deck-not-found-re-prompt-one}]}
                                  :possible_intents [{:intent (:text action-intents)}]}]))))

(defn- ask-first-question
  [conversation-id argument state]
  (build-assistant-response conversation-id
                            true
                            [{:input_prompt {:initial_prompts [{:ssml "Who was the first president of the united states?"}]
                                             :no_input_prompts [{:ssml responses/deck-found-re-prompt-one}]}
                              :possible_intents [{:intent (:text action-intents)}]}]))

;; Main Actions

(def ^:private step-actions {:get-deck get-deck
                             :ask-first-question ask-first-question})

(defn- text-action
  [arguments conversation user]
  (let [conversation-id (:conversation_id conversation)
        {state :state
         step :step} (state/get-conversation conversation-id)
        step-action (step step-actions)]
    (step-action conversation-id arguments state)))

(defn- main-action
  [arguments conversation user]
  (let [{conversation-id :conversation_id} conversation]
    (state/initialize-conversation-state conversation-id)

    (build-assistant-response (:conversation_id conversation)
                              true
                              [{:input_prompt {:initial_prompts [{:ssml responses/main-prompt}]
                                               :no_input_prompts [{:ssml responses/main-re-prompt-one}]}
                                :possible_intents [{:intent (:text action-intents)}]}])))

(def action-map {(:main action-intents) main-action
                 (:text action-intents) text-action})
