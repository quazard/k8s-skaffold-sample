# K8s Skaffold Sample

This sample project was created to play a bit with [Skaffold](https://skaffold.dev/) and Kubernetes.

## How to use it

* Download the project and execute the following command from within the project folder
    ```sh
    skaffold dev --port-forward=true
    ```

This will build, deploy and expose the service endpoints through the port `9000`.

Skaffold will look for any change made within the project folder and rebuilt/deploy itself.

## How to Init the Skaffold config

The configuration is already provided through the skaffold.yaml file, but bellow are the steps to configure it at first time.

* Create a Dockerfile and a k8s deployment.yaml file
    ```sh
    kubectl create deploy skaffold-sample --image=skaffold/sample --dry-run=client -o=yaml > deploy.yaml
    ```
* Next execute the Skaffold init command, by indicating the k8s manifests location
    ```sh
    skaffold init -a='{ "builder": "Docker", "payload": { "path": "./Dockerfile" }, "image": "skaffold/sample" }'
    ```

    in this case, i had to tell where to find the Dockerfile and how to name the resulting image.
* Finally, a config was added to the skaffold.yaml file to let Skaffold know how to handle the port forward flag
    ```yaml
    portForward:
    - resourceType: deployment
      resourceName: skaffold-sample
      port: 8080
      localPort: 9000
    ```