# Linux Notes

## Basic commands
    ifconfig | grep "inet " | grep -v "127.0"
    cat /etc/os-release
    cat /etc/group
    cat /etc/passwd
    Check current shell: `ps -p $$`
    sudoer=$USER && sudo sh -c "echo \"$sudoer ALL=(ALL:ALL) NOPASSWD:ALL\" > /etc/sudoers.d/$sudoer"

[comment]: # (check ports)
    netstat -tulpn

## mac os command line
[comment]: # (reset python default library)
    brew install python && brew link --overwrite python && hash -r python
    lsof -i -n -P | grep TCP | grep 5432  # check tcp port

## alpine linux command line
    shutdown: reboot / halt / poweroff
    release version: cat /etc/alpine-release

[comment]: # (app management)
    apk update
    apk upgrade --available

    apk -vv info|sort
    apk cache clean

    apk add openssh openntp vim
    apk del openssh openntp vim

    apk search -v 'acf*'
    apk search -v --description 'NTP'

    apk info -a zlib  # list installed

[comment]: # (docker container)    
    apk --no-cache --repository http://dl-cdn.alpinelinux.org/alpine/edge/community add dumb-init

[comment]: # (postgres)
    apk add postgresql # postgresql-dev
    su - svc -c "postgresql"

### debian linux
    dpkg-query -l  
    apt list --installed
    apt-get update -yqq && apt-get install nano
    apt-get update && apt-get upgrade
    apt-get install build-essential

## redhat linux
[comment]: # (check version)
    cat /etc/redhat-release

[comment]: # (Check repo and EPEL repository)
    yum repolist
    wget https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm
    yum install epel-release-latest-7.noarch.rpm

[comment]: # (Update and list installed)
    yum -y update
    yum list installed

    yum install wget
    yum install python-pip

[comment]: # (find available package then install)
    yum list postgresql*
    yum install <postgresql96-server>

[comment]: # (prepare to build python)
    yum groupinstall -y development
    yum install yum-utils
    yum install libpqxx-devel

[comment]: # (not sure if this is needed)
    yum-builddep python
    yum install python-devel

[comment]: # (download python 3.4.2)
    wget https://www.python.org/ftp/python/3.4.2/Python-3.4.2.tgz


## ubuntu
[comment]: # (installation issue with 14.04)

    cat /etc/os-release
    apt list --installed
    apt-get update && apt-get upgrade

    apt-get install mlocate && updatedb

    apt-get -y install python-pip
    apt-get install -y python-dev python-setuptools build-essential libpq-dev

    pip install  --ignore-installed airflow[jdbc,hdfs,hive,postgres]
    useradd airflow
    deluser --remove-home airflow
