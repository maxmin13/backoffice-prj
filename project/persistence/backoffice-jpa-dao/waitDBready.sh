#!/bin/bash

# wait that Mariadb is ready to accept requests,
# then let the build proceed and the integration tests run.

IMAGE_NAME="${1}" 
HOST="${2}"       
DB_NAME="${3}"    
USER="${4}"       
PWD="${5}"       

get_container_status() {
  local image_name="${1}"
  local container_id
  container_id=$(docker ps --all --quiet --filter ancestor="${image_name}" --format="{{.ID}}" | head -n 1)
  local container_status
  container_status="$(docker inspect --format "{{json .State.Status }}" "${container_id}")"
  echo "${container_status}"
}

status="$(get_container_status "${IMAGE_NAME}")"

until [ "${status}" == '"running"' ]
do
  echo "${status}"
  echo "Waiting for container to start..."
  sleep 5
  status="$(get_container_status "${IMAGE_NAME}")"
done

echo "Container running, checking database ..."

RETRY_COUNT=0
RETRY_MAX=10
RETRY_INTERVAL=5

while ! mariadb -h "$HOST" -u "$USER" -p"$PWD" "$DB_NAME" 2>/dev/null; do
  RETRY_COUNT=$(("${RETRY_COUNT}" + 1))
  if [ ${RETRY_COUNT} -ge ${RETRY_MAX} ]; then
    echo "Mariadb not ready after ${RETRY_MAX} attempts. Exiting."
    exit 1
  fi
  echo "Waiting for Mariadb to be ready... Attempt: ${RETRY_COUNT}"
  sleep "${RETRY_INTERVAL}"
done


