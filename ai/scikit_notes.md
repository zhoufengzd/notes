# scikit-learn notes
* supoort: classification, regression, dimensionality reduction, and clustering.

### references
* Mastering Machine Learning with scikit-learn by Gavin Hackeling
* Scipy Lecture Notes: http://www.scipy-lectures.org/index.html
* public data:
    * UC Irvine Machine Learning Repository
    * Kaggle Datasets
    * data.gov

## Linear Regression

## From Linear Regression to Logistic Regression

## Nonlinear Classification and Regression with Decision Trees

## Clustering

### K-Means
* O(n).
    * 1. group points by distance -> partition center.
    * 2. adjust partition center by taking average of the distance
    * 3. repeat step 1 and 2 until no more adjustment of the partition center
* usually very fast, though worst case can be very slow to converge
* needs pre decide number of partitions
* variations: K-Medians

### Mean-Shift Clustering

### dbscan: density-based spatial clustering of applications with noise
* Find the ε (eps) neighbors of every point, and identify the core points with more than minPts neighbors.
    * -- eps: maximum distance between two nodes
* Find the connected components of core points on the neighbor graph, ignoring all non-core points.
* Assign each non-core point to a nearby cluster if the cluster is an ε (eps) neighbor, otherwise assign it to noise.
* con: doesn’t perform as well as others when the clusters are of varying density.

### Gaussian Mixture Model (GMM)
* expectation–maximization (EM) clustering using gaussian mixture models (GMM)
* data: Gaussian / Gauss or normal distribution
* clustering: use the mean and the standard deviation
* pooled population / unsupervised learning
* examples:
    * financial model
    * housing prices
    * topics in a document
* Dirichlet process: P{probability}
* variational: bayesian gaussian mixture


## Feature extraction

### Text files / Bag-of-words
*  high-dimensional sparse datasets
    * n_features: number of unique words
    * n_samples: doc sample count
    * X[doc_id, word_id] = word_count
    * Vocabulary:
        * Term frequency (tf)
        * Term Frequency times Inverse Document Frequency: tf-idf. inverted index.
    * stop word / stemming with natural language tool kit (NTLK)
* Training a classifier
* Building a pipeline

### extracting features from images

## Dimensionality Reduction with Principal Component Analysis (PCA)

## The Perceptron

## From the Perceptron to Support Vector Machines

## From the Perceptron to Artificial Neural Networks

## classes
* base.py
```
class LinearClassifierMixin(ClassifierMixin):
    def decision_function(self, X):
        """Predict confidence scores for samples.
        The confidence score for a sample is the signed distance of that sample to the hyperplane.
        X : {array-like, sparse matrix}, shape = (n_samples, n_features)
        scores = safe_sparse_dot(X, self.coef_.T,
                                 dense_output=True) + self.intercept_
        return scores
```
* dict_vectorizer.py
    * Compressed Sparse Row Format (CSR)
    * three NumPy arrays: indices, indptr, data
        * indices is array of column indices
        * data is array of corresponding nonzero values
        * indptr points to row starts in indices and data
```
class DictVectorizer(BaseEstimator, TransformerMixin):
    """Transforms lists of feature-value mappings to vectors."""
    def _transform(self, X, fitting):
        ...
        # collect all the possible feature names and build sparse matrix
        for x in X:
            for f, v in six.iteritems(x):
                if isinstance(v, six.string_types):
                    f = "%s%s%s" % (f, self.separator, v)
                    v = 1
                if f in vocab:
                    indices.append(vocab[f])
                    values.append(dtype(v))
                else:
                    if fitting:
                        feature_names.append(f)
                        vocab[f] = len(vocab)
                        indices.append(vocab[f])
                        values.append(dtype(v))

            indptr.append(len(indices))

        indices = np.frombuffer(indices, dtype=np.intc)
        indptr = np.frombuffer(indptr, dtype=np.intc)
        shape = (len(indptr) - 1, len(vocab))

        result_matrix = sp.csr_matrix((values, indices, indptr),
                                      shape=shape, dtype=dtype)
        return result_matrix
```
* metaestimators.py
```
```
* parallel.py
```
class Parallel(Logger):
    ''' Helper class for readable parallel mapping.

```
* pipeline.py
```
class Pipeline(_BaseComposition):
    def predict_proba(self, X):
        Xt = X
        for name, transform in self.steps[:-1]:
            if transform is not None:
                Xt = transform.transform(Xt)
        ## Xt: csr_matrix
        return self.steps[-1][-1].predict_proba(Xt)

    def fit(self, X, y=None, **fit_params):
        Xt, fit_params = self._fit(X, y, **fit_params)  # transform the data
        self._final_estimator.fit(Xt, y, **fit_params)  # fit

    def fit_transform(self, X, y=None, **fit_params):
        # Xt : array-like, shape = [n_samples, n_transformed_features]
        last_step = self._final_estimator   ## like DictVectorizer
        Xt, fit_params = self._fit(X, y, **fit_params)
        return last_step.fit_transform(Xt, y, **fit_params)

class FeatureUnion(_BaseComposition, TransformerMixin):
    def transform(self, X):
        """Transform array X => X_t (shape with n_samples, sum_n_components)
        Xs = Parallel(n_jobs=self.n_jobs)(
            delayed(_transform_one)(trans, weight, X)
            for name, trans, weight in self._iter())
```

### some terms
* sparse vectors: High-dimensional feature vectors that have many zero-valued elements
* curse of dimensionality, or the Hughes effect: dimensionality increases, more training data.
* CSR: compressed sparse row format
