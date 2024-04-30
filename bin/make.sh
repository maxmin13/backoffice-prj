#!/bin/bash 
# shellcheck disable=SC1091

##############################################################################
# The script creates a datacenter on AWS and installs the backoffice
# application.
#
# run:
#
# export AWS_ACCESS_KEY_ID=xxxxxx
# export AWS_SECRET_ACCESS_KEY=yyyyyy
# export AWS_DEFAULT_REGION=zzzzzz
#
# ./make.sh
#
##############################################################################

set -o errexit
set -o pipefail
set -o nounset
set +o xtrace

WORKSPACE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && cd ../.. && pwd)"

# directory where the datacenter project is downloaded from github
# see: name_backoffice_box file
DATACENTER_DIR="${WORKSPACE_DIR}"/datacenter-prj

# directory where the messagboard project is downloaded from github
# see: name_backoffice_box file
BACKOFFICE_DIR="${WORKSPACE_DIR}"/backoffice-prj

export BACKOFFICE_DIR
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
echo "Creating AWS datacenter ..."

cd "${DATACENTER_DIR}"/bin
chmod 755 make.sh
./make.sh

echo "AWS datacenter created."

# copy the private key from datacenter-prj
private_key_nm='backoffice-box'
if [[ -f "${DATACENTER_DIR}/access/${private_key_nm}" ]]
then
	rm -f "${BACKOFFICE_DIR}/access/${private_key_nm}"
	cp "${DATACENTER_DIR}/access/${private_key_nm}" "${BACKOFFICE_DIR}"/access/ 
	echo "Private key copied to the backoffice project."
fi

echo "Private key directory ${BACKOFFICE_DIR}/access"
echo "Provisioning the instance ..."

cd "${DATACENTER_DIR}"/bin
chmod 755 provision.sh
./provision.sh

echo "AWS datacenter provisioned."

###########################
#      BACKOFFICE APP     #
###########################

echo "Deploying backoffice application ..."

cd "${BACKOFFICE_DIR}"/bin

chmod 755 provision.sh
##########./provision.sh

if [[ -d "${DATACENTER_DIR}" ]]
then
  rm -rf "${DATACENTER_DIR}"
fi
  
echo "Backoffice application installed."  
  
  
