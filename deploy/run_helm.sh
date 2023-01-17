#!/usr/bin/env sh

set -e

for DIR in ./* ; do
  [ ! -d "${DIR}" ] && continue
  echo "Helm building dependencies"
  helm dependency update "${DIR}"

  echo "Looking for subcharts"
  for SUB_DIR in ${DIR}/charts/* ; do
    [ ! -d "${SUB_DIR}" ] && continue
    echo "Helm building dependencies"
    helm dependency update "${SUB_DIR}"
  done
  
done

helm install --atomic zipkin ./zipkin # distributed tracing
helm install --atomic rabbitmq ./rabbitmq # messaging
helm install --atomic logstash ./logstash # from elk stack
helm install --atomic kube-prometheus-stack ./kube-prometheus-stack
helm install --atomic car-rental ./umbrella-chart