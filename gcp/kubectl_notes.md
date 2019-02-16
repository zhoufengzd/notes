# Kubernetes
* automating deployment, scaling and management of containerized applications
* design: loosely coupled
* labels: to any API object in the system

## setup:
* gcloud components install kubectl

## Pod
* basic scheduling unit
* matches 1~N application containers (such as Docker or rkt)
*  not designed to be persistent

## Controller
* Replication Controller
* DaemonSet Controller
* Job Controller

## Service
* a set of pods that work together, such as one tier of a multi-tier application.
* defined by a label
* default: service discovery + load balance

## Kubernetes node
* also known as Worker or Minion.  
* the single machine (or virtual machine) where containers(workloads) are deployed
* by default, running the following:
```
    gcr.io/google_containers/kube-proxy
    gcr.io/google_containers/heapster-amd64
    gcr.io/google_containers/cluster-proportional-autoscaler-amd64
    gcr.io/google-containers/fluentd-gcp
    gcr.io/google_containers/k8s-dns-sidecar-amd64
    gcr.io/google_containers/k8s-dns-kube-dns-amd64
    gcr.io/google_containers/k8s-dns-dnsmasq-nanny-amd64
    gcr.io/google_containers/addon-resizer
    gcr.io/google_containers/pause-amd64
```

## Kubelet
* pod monitors. restarts the pod

## Kube-proxy
* network proxy and a load balancer,

## cAdvisor
* usage and performance monitor

## Load balancing
* kube-proxy:
    * default mode: iptables. an incoming request goes to a randomly chosen pod within a service
    * older mode: userspace. uses round-robin load distribution.
* ingress: http / https traffic. rules defined in ingress resource.
* loadBalancer: tcp.
