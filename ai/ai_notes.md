# Machine learning notes
* Tom Mitchell: "A program can be said to learn from experience E with respect to some class of tasks T and performance measure P, if its performance at tasks in T, as measured by P, improves with experience E."
* supervised vs unsupervised learning
* data set: training set(50%) / test set (25%) / validation set (25%)
* Balancing art: memorization / over-fitting vs generalization / under-fitting

## Tools
* tensorflow
* scikit
* orange: https://orange.biolab.si/
* dmlc xgboost
* dask: flexible parallel computing library for analytics

## machine learning tasks
* clustering:
* dimensionality reduction:
* bias and variance:
    * accuracy: ACC = (TP + TN) / (TP + TN + FP + FN)
    * precision: P = TP / (TP + FP)

## feature engineering and kernel method
* "Applied machine learning" is basically feature engineering. -- Andrew Ng
* kernel methods require only a user-specified kernel, like similarity function

### kernel method
* a class of algorithms for pattern analysis (support vector machine / SVM)


## stages:
* pre-process: dataframe
    * example: ML api as preprocessing engine. language api to tokenize asian language
* training
* prediction

## ML Crash Course
* https://developers.google.com/machine-learning/crash-course

### Terms:
* Labels: Y. The thing we're predicting.
* Features: input variables (x1,x2..xn)
* Examples: particular instance of data, x
    * labeled examples: {features: [x1..], label: y}
    * unlabeled examples: {features: [x1..], label: ?}
* Models: defines the relationship between features and label
    * training: creating or learning the model
    * interface: apply the trained model to unlabeled examples
* Regression vs. classification:
    * classification: predict discrete values
    * regression: predict the value of a continuous response variable.

### Linear Regression:
* y (label) = bias + weight * x (feature)
* Loss: minimizes loss.
    * Mean square error (MSE): Sum( (y - f(x..)) * (y - f(x..)) ) / N (samples)

### Feature Engineering

### Neural Network

## Google next
* 7 must-see sessions on data analytics at Next '18
    * Migrating Hadoop to Google Cloud Platform (with Dataproc)
    * Data and Analytics Platform Overview and Customer Examples
    * Data Warehousing Migrations: Lessons from Home Depot
    * Predicting Community Engagement on Reddit: TensorFlow, GDELT and Dataflow
    * Building the World's Largest Enterprise Data Warehouse
* David Cournpeau: OCR / fiancial time series forecasting
    * https://github.com/cournape
    * don't over architect (yahoo / facebook < 100G)
    * https://github.com/GoogleCloudPlatform/ml-on-gcp/tree/master/gce/burst-training
    * Deep learning VM image
    * Ocado retail: retail and ai
        * XG
        * C5 rule engine / C4.5 algorithm
        * Decision Tree Scikit Learn /
        * Random Forest: Spark -> Scikit learn
        * Neural Network (TensorFlow)
    * Neural networks are not necessary for every ML problem
    * Cloud ML Engine XGBoost and scikit-learn
    * stevegreenberg@google.com
* Jia Li: AutoML. ML + Domain Knowledge
    * Keller Williams Realty: image recognition
    * AutoML:packages with
        * Natural Language: terms.
        * Translation:
        * Vision
    * TPU: ebay
    * Tensorflow: Ocado
    * AutoML: blum. No in-house ML
    * ML API:
        * Vision / Speech / Natural Language / Video / Translation
        * Bloomberg
* Fei-Fei Li:
    * World Wildlife Fund: track tiger / rhinos
* Janet Mann: preserving wild life in ocean.
* How Publishers Can Take Advantage of Machine Learning
    * DFP ad targeting
    * language segmentation
    * recommendation: click through rate to check result
    * speech / video intelligence API: video -> speech -> text -> NLP
    * TensorFlow
    * personalized recommendation engine / reading habits
    * propensity modeling / Forescasting / prediction
    * Churn Prediction: binary logistic regression
    * ETL / airflow -> bq -> preprocess / features -> create model -> ...
    * Kaggle StumbleUpon evergreen data set
    * outside bigquery: spark / Dataproc dedup
* Applying Google Cloud AI to Enterprise Content in Box
    * Box skills
    * Pick the right problems
* Caching Made Easy, with Cloud Memorystore
    * OpenCensus

## TinyML
* https://hackaday.io/courses