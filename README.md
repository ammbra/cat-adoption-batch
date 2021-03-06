# Spring Batch with Java 17 and JKube 


## Prerequisites
- Java17 
- Maven 3.8.1
- A running Docker daemon
- Access to a Kubernetes cluster (Minikube/OpenShift) to deploy the resources

## How to run the example

###Locally

Start the Spring Boot application from [CatAdoptionBatchApplication.java](src/main/java/com/example/cat/adoption/CatAdoptionBatchApplication.java).

Locally you can check the input in [report.xml](src/main/resources/xml/report.xml) and output in [report.csv](csv/report.csv).

### Building and pushing the container image

Before executing this step, connect to your container image registry and fill in your repository details in [pom.xml](pom.xml):

````
<registry>[your_registry here]</registry>
<repository>[your_repo_here]</repository>
````

You can package your jar application, build and push the container image by running:

``
mvn clean package k8s:build k8s:push
``

### Generate Kubernetes manifest and work with them

You can generate Kubernetes resources by running:

``
mvn k8s:resource
``

If you are connected to a Kubernetes or OpenShift cluster, you can run:

````
mvn k8s:apply
##or you can run
# mvn k8s:deploy
````

You can undeploy your previous resource deployment by running ``mvn k8s:undeploy``.

## Inspect the output

You can checkout the logs of the Pod generated by the Job by running:

```
kubectl logs $(kubectl get pod -l app=cat-adoption-batch  -o custom-columns=:metadata.name)
```

And you can checkout the output by running:

```
kubectl exec $(kubectl get pod -l app=helper -o custom-columns=:metadata.name) -- cat sample/report.csv 
```

If you would like to copy locally the output from the PersistentVolume, you can use the following command:
```
kubectl cp helper:sample .
```
