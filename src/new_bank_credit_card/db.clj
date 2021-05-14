(ns new-bank-credit-card.db
  (:import [java.util UUID]))

;; DEFINITIONS

(def purchase-categories
  #{:food :eletronic :bills :bavarage-and-restaurant :other})

(def currencies
  #{:BRL :USD})

;; Internals

(def danilo
  {:uuid (UUID/randomUUID)
  :name "Danilo Santos da Silva"
  :cpf "00000000000"
  :email "danilo@mail.com"})

(def maria
  {:uuid (UUID/randomUUID)
  :name "Maria Alberta de Souza"
  :cpf "00000000001"
  :email "maria@mail.com"})

(def joao
  {:uuid (UUID/randomUUID)
   :name "Joao Henrique Santos"
   :cpf "00001000000"
   :email "joao@email.com"})


;; CARDS

(def card-danilo
  {:uuid (UUID/randomUUID)
   :card-number "1234"
   :cvv 234
   :total-limit 2000
   :client-uuid (:uuid danilo)})

(def card-maria
  {:uuid (UUID/randomUUID)
   :card-number "12345"
   :total-limit 2000
   :cvv 234
   :client-uuid (:uuid maria)})

(def card-joao
  {:uuid (UUID/randomUUID)
   :card-number "123456"
   :cvv 333
   :total-limit 2000
   :client-uuid (:uuid joao)})


;; OPERATION

(def operation1
  {:timestamp nil
   :status :approved
   :operation :purchase
   :category :food
   :value  -23
   :currency :BRL
   :card-uuid (:uuid card-danilo)})  

(def operation2
  {:timestamp nil
   :status :approved
   :operation :chargeback
   :category nil
   :value 300
   :currency :BRL
   :card-uuid (:uuid card-danilo)})

(def operation3
  {:timestamp nil
   :status :approved
   :operation :purchase
   :category :eletronic
   :value -200
   :currency :BRL
   :card-uuid (:uuid card-danilo)})

(def operation4
  {:timestamp nil
   :status :approved
   :operation :bill-payment
   :category nil
   :value 300
   :currency :BRL
   :card-uuid (:uuid card-maria)})

(def operation5
  {:timestamp nil
   :status :approved
   :opereation :bill-payment
   :category nil
   :value 3000
   :currency :BRL
   :card-uuid (:uuid card-maria)})

(def operation6
  {:timestamp nil
   :status :approved
   :opereation :purchase
   :category :food
   :value 500
   :currency :BRL
   :card-uuid (:uuid card-maria)})


(defn get-entity-by
  "Retrieve an entity by a specific key"
  [coll key query]
  (->> coll
       (filter (fn [entry] (= (key entry) query)))))

;; API

(defn get-all-clients
  "Retrieve all clients from database"
  []
  [danilo maria joao])

(defn get-all-cards
  "Retrieve all cards from database"
  []
  [card-danilo card-maria card-joao])

(defn get-all-operations
  "Retrieve all Operations"
  []
  [operation1 operation2 operation3 operation4 operation5 operation6])

(defn get-client-by
  "Filter the clients collecion"
  [key query]
  (get-entity-by (get-all-clients) key query))


(defn get-card-by
  "Filter cards collection"
  [key query]
  (get-entity-by (get-all-cards) key query))


(defn get-operations-by
  "Filter operations collection"
  [key query]
  (get-entity-by (get-all-operations) key query))


(defn get-client-by-uuid
  "Returns a client collection quering by the client's uuid"
  [client-uuid]
  (->> client-uuid
       (get-client-by :uuid)))

(defn get-client-by-cpf
  "Query clients collection quering by client's social security id (CPF)"
  [client-cpf]
  (->> client-cpf
       (get-client-by :cpf)))

(defn get-client-by-email
  "Return a coll of clients given an email address"
  [client-email]
  (->> client-email
       (get-client-by :email)))

(defn get-cards-by-client
  "Query cards collection quering by a given client"
  [client]
  (->> client
       (:uuid)
       (get-card-by :client-uuid)))

(defn get-operations-by-card
  "Get all the operations done by a given card"
  [card]
  (->> card
       (:uuid)
       (get-operations-by :card-uuid)))

(defn get-card-operations-by-operation-type
  [op card]
  (->> card
       (get-operations-by-card)
       (filter #(= (:operation %) op))))

(defn get-purchases-grouped-by-category
  [card]
  (->> card
       (get-card-operations-by-operation-type :purchase)
       (group-by :category)))

