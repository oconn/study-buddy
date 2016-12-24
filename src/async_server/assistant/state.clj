(ns async-server.assistant.state)

(def ^:private
  presidents
  [{:id 0
    :question "Who was the first president?"
    :answer "George Washington"}])

(def ^:private initial-deck-state {:presidents presidents})

(def ^:private initial-app-state {:decks initial-deck-state
                                  :conversations {}})

(def ^:private initial-conversation-state {:step :get-deck
                                           :state {:selected-deck nil
                                                   :answered-correct 0
                                                   :answered-incorred 0}})

(def app-state (atom initial-app-state))

;; State accessors

(defn get-app-state []
  @app-state)

(defn get-conversation
  [conversation-id]
  (get-in @app-state [:conversations (keyword conversation-id)]))

(defn get-conversation-state
  [conversation-id]
  (:state (get-conversation conversation-id)))

(defn get-conversation-step
  [conversation-id]
  (:step (get-conversation conversation-id)))

(defn get-deck
  [deck-name]
  (get-in @app-state [:decks deck-name]))

;; State setters

(defn initialize-conversation-state
  [conversation-id]
  (swap! app-state assoc-in [:conversations
                             (keyword conversation-id)] initial-conversation-state))

(defn set-next-step
  [conversation-id next-step]
  (swap! app-state assoc-in [:conversations
                             (keyword conversation-id)
                             :step] next-step))

(defn set-next-state
  [conversation-id next-state]
  (swap! app-state assoc-in [:conversations
                             (keyword conversation-id)
                             :state] next-state))
