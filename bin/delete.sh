#!/bin/bash 
# shellcheck disable=SC1091

##############################################################################
# The script deletes a datacenter on AWS.
#
# run:
#
# export AWS_ACCESS_KEY_ID=xxxxxx
# export AWS_SECRET_ACCESS_KEY=yyyyyy
# export AWS_DEFAULT_REGION=zzzzzz
#
# ./delete.sh
#
##############################################################################

set -o errexit
set -o pipefail
set -o nounset
set +o xtrace

WORKSPACE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && cd ../.. && pwd)"

# directory where the datacenter project is downloaded from github
DATACENTER_DIR="${WORKSPACE_DIR}"/datacenter-prj

# directory where the backoffice project is downloaded from github
BACKOFFICE_DIR="${WORKSPACE_DIR}"/backoffice-prj

export DATACENTER_DIR

#######################################
# DATACENTER ENVIRONMENT AND INSTANCE #
#######################################

cd "${WORKSPACE_DIR}"

if [[ ! -d "${DATACENTER_DIR}" ]]
then
  git clone git@github.com:maxmin13/datacenter-prj.git 

  echo "AWS datacenter project cloned."
else
  echo "AWS datacenter project already cloned."
fi

# override the default configuration files in datacenter-prj.
cp "${BACKOFFICE_DIR}/config/datacenter.json" "${DATACENTER_DIR}"/config
cp "${BACKOFFICE_DIR}/config/hostedzone.json" "${DATACENTER_DIR}"/config
cp "${BACKOFFICE_DIR}/provision/inventory/group_vars/name_backoffice_box" "${DATACENTER_DIR}"/provision/inventory/group_vars

echo "Datacenter config files set."
echo "Deleting AWS datacenter ..."

cd "${DATACENTER_DIR}"/bin
chmod 755 delete.sh
./delete.sh

if [[ -d "${DATACENTER_DIR}" ]]
then
  rm -rf "${DATACENTER_DIR}"
fi

echo "AWS datacenter deleted."

  
