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

## 1. Setup
### Setup tools
* distutils: the standard / older tool for packaging in Python
* setuptools package
    * command line: easy_install
    * distribute: fork of setuptools. deprecated
* pip: higher-level interface on top of setuptools or Distribute

### Base setup: pip / virtualenv
* install python: download packages. optional: installing from sources
    * installed directory: /Library/Frameworks/Python.framework/Versions/<major>.<minor>
```
apt-get -y install python3-pip
pip3 install pip
pip install virtualenv  ## install virtualenv
ln -s /Library/Frameworks/Python.framework/Versions/Current/bin/virtualenv virtualenv

# Check
brew info python
ls -al /usr/bin/python*  ## system python
ls -al /usr/local/bin/python*   ## All python versions installed via Homebrew
```

### virtualenv:
* creates bin / include / lib directories, updates PATH
    * bin: copied pip / python wheel and activate
    * include: link to /Library/Frameworks/Python.framework/Versions/<major>.<minor>/include
    * lib: links to /Library/Frameworks/Python.framework/Versions/<major>.<minor>/lib/python<major>.<minor>/*

```
# command
virtualenv -p /usr/local/bin/python2.7 py27
virtualenv -p /usr/local/bin/python3.4 py34
virtualenv -p /usr/local/opt/python3/bin/python3 py3  ## latest python3
pip install --upgrade pip setuptools
source py34/bin/activate | deactivate
```

### pip
```
pip install -r requirements.txt
pip freeze --local | grep -v '^\-e' | cut -d = -f 1  | xargs -n1 pip install --upgrade ## upgrade all

* check package dependencies
pip install pipdeptree
pipdeptree
```

### pyenv & pyenv-virtualenv
```
brew install pyenv
brew install pyenv-virtualenv

# List and install python
pyenv install -l
pyenv install 3.5.5
pyenv install 3.6.4
  # -- install framework build
PYTHON_CONFIGURE_OPTS="--enable-framework" pyenv install 3.6.4

# Prepare pyenv settings
pyenv_profile="$HOME/.pyenv/pyenv_env"
pyenv init - > $pyenv_profile
pyenv virtualenv-init - >> $pyenv_profile

# setup env
pyenv virtualenv 3.6.5 quant36
```

### pipenv
```
pip install pipenv
pipenv shell
```

### ipython / jupyter notebook
* jupyter = Julia + Python + R
```*jupyter notebook ```

## 2. Python Working Environment
* bin: executables
    * /Library/Frameworks/Python.framework/Versions/<major>.<minor>/bin
    * <virtual_env>/bin
* site packages:
    * /Library/Frameworks/Python.framework/Versions/<major>.<minor>/lib/python<major>.<minor>/site-packages
    * <virtual_env>/lib/python<major>.<minor>/site-packages/
    * check: `python -m site --user-site`

### Modules
* pure Python modules - modules contained in a single .py file
* extension modules - modules written in low-level languages like C and have extensions such as .so, .pyd
* packages - a directory with __init__.py

### Install packages
* Installing Packages from source distributions: `python setup.py install`
* Installing Packages from PyPI: pip install ...

### Most used packages:
* nose: test
* flask: extensible web microframework
* marshmallow: serialization
* jinja: template engine
    * Environment (template locations, etc)
        * Loaders: FileSystemLoader, PackageLoader, and DictLoader.
    * template

## 3. Package python code
* https://code.tutsplus.com/tutorials/how-to-write-your-own-python-packages--cms-26076

### structure
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

### distributions
* source distribution: python setup.py sdist
* wheel distribution: python setup.py bdist_wheel

## 4. python libraries
### flask
* micro web framework = Werkzeug toolkit (WSGI) + Jinja2

### prometheus
* open-source systems monitoring and alerting toolkit. originally built at SoundCloud.
