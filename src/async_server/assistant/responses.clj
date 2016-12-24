(ns async-server.assistant.responses)

(def main-prompt "Hi!<break time=\"1\" /> What would you like to study")
(def main-re-prompt-one "I didn't hear anything, what would you like to study")

(defn deck-found [deck-name] (str deck-name " it is.<break time=\"1\" /> Let me pull up the questions. <break time=\"1\" /> Say start when you are ready."))
(def deck-found-re-prompt-one "Say start when you are ready")

(defn deck-not-found [deck-name] (str "Sorry <break time=\"1\" /> I was unable to find the " deck-name " deck. <break time=\"1\" /> Please choose another deck."))
(def deck-not-found-re-prompt-one "Please choose a new deck")
