(ns new-bank-credit-card.core
  (:gen-class)
  (:require [new-bank-credit-card.db :as db]
            [new-bank-credit-card.logic :as logic]))

(defn -main
  [& args]
  (println "Welcome to New Bank!"))

(defn get-client-cards
  [cpf]
  (let [client (db/get-client-by-cpf cpf)]
    (db/get-card-by-client client)))

(defn card-limit
  [card]
  (let [total-limit (:total-limit card)
        card-operations (db/get-operations-by-card card)]
    (logic/calculate-card-limit total-limit card-operations)))

(defn spendings-by-category
  [card]
  (let [spending-by-cat-summary (db/get-purchases-grouped-by-category card)]
    logic/calculate-spending-by-category spending-by-cat-summary))


