(ns new-bank-credit-card.logic)

(defn calculate-card-limit
    [total-limit op-coll]
    (->> op-coll
         (map :value)
         (reduce + total-limit)))

(defn sum-op-values
  [op-coll]
  (->> op-coll
       (map :value)
       (reduce + 0)))

(defn calculate-spending-by-category
  [spending-summary]
  (->> spending-summary
       (reduce (fn [summary [cat ops]]
                 (assoc summary cat (spending-summary ops)))
               {})
       (into {})))

(defn op-validation-function
  [& preds]
  (fn [] (->> (map (fn [validation] (apply (first validation) (rest validation))))
              (reduce and))))

(defn operation-aproval
  [validation-fn]
  (if (validation-fn)
    :aproved
    :denied
    ))

(defn create-new-operation
  [card operation category value currency]
  (let [approval-fn (op-validation-function  [[>= (:total-limit card) value]
                                               [(comp not empty?) value]
                                               [(comp not nil?) currency]
                                               [(comp not nil?  card)]
                                               [(comp not nil?) operation]])]
    (operation-aproval approval-fn)))