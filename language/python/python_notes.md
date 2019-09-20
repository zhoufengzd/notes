# Python Notes
* Python’s strengths
    * Batteries included: rich collection of numerical methods, plotting or data processing tools.
    * Easy to learn to data scientists
    * Easy communication: syntax is simple, avoiding strange symbols or lengthy routine specifications.
    * Efficient code: quick development times and quick execution times.
    * Universal on all platforms
* The Scientific Python ecosystem
    * Python
    * Core numeric libraries: Numpy, Scipy, Matplotlib
    * Advanced interactive environments: IPython, Jupyter, notebooks
    * Domain-specific packages:
        * Mayavi for 3-D visualization
        * pandas, statsmodels, seaborn for statistics
        * sympy for symbolic computing
        * scikit-image for image processing
        * scikit-learn for machine learning

## set up

#### Check installed pythons
```
brew install python
brew info python

ls -al /usr/bin/python*  ## system python
ls -al /usr/local/bin/python*   ## All python versions installed via Homebrew
```

### tools: pip, virtualenv
* distutils: the standard / older tool for packaging in Python
* setuptools package
    * command line: easy_install
    * distribute: fork of setuptools. deprecated
* pip: higher-level interface on top of setuptools or Distribute

```
apt-get -y install python3-pip
pip3 install pip
pip install virtualenv
ln -s /Library/Frameworks/Python.framework/Versions/Current/bin/virtualenv virtualenv
```

### use virtual env:
* creates bin / include / lib directories, updates PATH
* bin: executables
    * /Library/Frameworks/Python.framework/Versions/<major>.<minor>/bin
    * <virtual_env>/bin
* site packages:
    * /Library/Frameworks/Python.framework/Versions/<major>.<minor>/lib/python<major>.<minor>/site-packages
    * <virtual_env>/lib/python<major>.<minor>/site-packages/
    * check: `python -m site --user-site`

```
* virtualenv
virtualenv {{ venv }}   ## python 2.7
python -m venv {{ venv }}   ## python 3.7+
source {{ venv }}/bin/activate | deactivate
```

### pip
```
pip install -r requirements.txt
pip freeze --local | grep -v '^\-e' | cut -d = -f 1  | xargs -n1 pip install --upgrade ## upgrade all

* check package dependencies
pip install pipdeptree
pipdeptree
```

## Module and package
* pure Python modules - modules contained in a single .py file
* extension modules - modules written in low-level languages like C and have extensions such as .so, .pyd
* packages - a directory with __init__.py
    * installing from source: `python setup.py install`

### common packages:
* nose: test
* flask: web microframework = Werkzeug toolkit (WSGI) + Jinja2
* marshmallow: serialization
* jinja: template engine
    * Environment (template locations, etc)
        * Loaders: FileSystemLoader, PackageLoader, and DictLoader.
    * template
* prometheus: systems monitoring and alerting toolkit.

### how to package python code?
* https://code.tutsplus.com/tutorials/how-to-write-your-own-python-packages--cms-26076

#### structure
```
code_directory\*.py
LICENSE
MANIFEST.in
README.md
requirements.txt
setup.cfg
setup.py
tests\*.py
```
* MANIFEST.in: include files outside package (code directory)

#### distributions
* source distribution: python setup.py sdist
* wheel distribution: python setup.py bdist_wheel


## ipython / jupyter notebook
* jupyter = Julia + Python + R
```*jupyter notebook ```
