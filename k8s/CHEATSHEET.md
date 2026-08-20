# Kubernetes Cheat Sheet — Movie Booking App

## Deploy everything

```bash
docker build -t spring-boot-containerization-app:latest .
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/mongodb.yaml
kubectl apply -f k8s/app.yaml
```

Or apply the whole folder at once (order doesn't matter — k8s retries until dependencies exist):

```bash
kubectl apply -f k8s/
```

App is reachable at `http://localhost:30080`.

## See what's running

```bash
kubectl get pods                    # list pods + status
kubectl get pods -w                 # watch pods live (Ctrl+C to stop)
kubectl get deployments             # list deployments + replica counts
kubectl get svc                     # list services + ports
kubectl get all                     # everything at once
```

## Inspect / debug

```bash
kubectl describe pod <pod-name>     # events, image, probes, why it's pending/crashing
kubectl logs <pod-name>             # container stdout/stderr
kubectl logs -f <pod-name>          # follow logs live
kubectl exec -it <pod-name> -- sh   # shell into a running container
```

## Live demo moments

```bash
# Self-healing: kill a pod, watch k8s recreate it automatically
kubectl delete pod <app-pod-name>
kubectl get pods -w

# Scaling: no compose equivalent — instant horizontal scale
kubectl scale deployment moviebooking-app --replicas=4
kubectl get pods

# Rolling update: change the image tag, apply, watch old pods
# drain and new ones roll in with zero downtime
kubectl set image deployment/moviebooking-app app=spring-boot-containerization-app:v2
kubectl rollout status deployment/moviebooking-app
kubectl rollout undo deployment/moviebooking-app   # roll back if v2 was bad
```

## Config changes

```bash
kubectl edit configmap moviebooking-app-config      # edit live (opens $EDITOR)
kubectl rollout restart deployment moviebooking-app # pods don't auto-reload
                                                      # ConfigMap changes — restart to pick them up
```

## Clean up

```bash
kubectl delete -f k8s/              # delete everything created above
kubectl delete pvc mongodb-data     # only if you also want to wipe Mongo's data
```

## Concept mapping (Compose → Kubernetes)

| Compose | Kubernetes | Why |
|---|---|---|
| service | Deployment | Desired state that k8s continuously reconciles |
| `restart: unless-stopped` | Deployment self-healing | Crashed Pods restart/reschedule automatically |
| container | Pod | Smallest deployable unit |
| `ports:` mapping | Service (ClusterIP/NodePort) | Stable network identity + load balancing across replicas |
| Compose internal DNS | k8s Service DNS | Same idea — service name resolves inside the cluster |
| named volume | PersistentVolumeClaim | Storage lifecycle decoupled from Pod lifecycle |
| `environment:` block | ConfigMap | Config editable independently of the Deployment spec |
| `depends_on: service_healthy` | readinessProbe | Whether a Pod should receive traffic yet |
| (no equivalent) | livenessProbe | Detects a hung Pod and restarts it |
| single host only | multi-node cluster + scheduler | The actual reason to use k8s over Compose |
