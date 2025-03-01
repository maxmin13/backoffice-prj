#!/bin/bash 
# shellcheck disable=SC1091

set -o errexit
set -o pipefail
set -o nounset
set +o xtrace

#########################################################################
## The script provisions an AWS instance using Ansible.
## Ansible connects to the instances with an SSH connection plugin.
## Ansible is configured to use the Dynamic inventory plugin aws_ec2 to create a dynamic 
## inventory of the AWS instances.
## see: ansible.cfg file
##
## run:
##
## AWS IAM user credentials
## export AWS_ACCESS_KEY_ID=xxxxxx
## export AWS_SECRET_ACCESS_KEY=yyyyyy
## export AWS_DEFAULT_REGION=zzzzzz
##
## export DATACENTER_DIR=<directory where the datacenter project is cloned>
##
## ./provision.sh 
##
#########################################################################

# AWS IAM user credientials
if [[ ! -v AWS_ACCESS_KEY_ID ]]
then
  echo "ERROR: environment variable AWS_ACCESS_KEY_ID not set!"
  exit 1
fi

if [[ ! -v AWS_SECRET_ACCESS_KEY ]]
then
  echo "ERROR: environment variable AWS_SECRET_ACCESS_KEY not set!"
  exit 1
fi

if [[ ! -v AWS_DEFAULT_REGION ]]
then
  echo "ERROR: environment variable AWS_DEFAULT_REGION not set!"
  exit 1
fi

# directory where the datacenter project is downloaded from github
# see: name_backoffice_box file
if [[ ! -v DATACENTER_DIR ]]
then
  echo "ERROR: environment variable DATACENTER_DIR not set!"
  exit 1
fi

# directory where the backoffice project is downloaded from github
# see: name_backoffice_box file
export BACKOFFICE_DIR
BACKOFFICE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && cd .. && pwd)"

echo "Creating local backoffice virtual environment ..."

{
    python -m venv "${BACKOFFICE_DIR}"/.venv
    source "${BACKOFFICE_DIR}"/.venv/bin/activate
    python3 -m pip install -r "${BACKOFFICE_DIR}"/requirements.txt
    deactivate
} > /dev/null

echo "Local backoffice virtual environment created."

ANSIBLE_PLAYBOOK_CMD="${BACKOFFICE_DIR}"/.venv/bin/ansible-playbook

cd "${BACKOFFICE_DIR}"/provision || exit

############################
#####  UPDATE SYSTEM   #####
############################

echo "Upgrading instances ..."

"${ANSIBLE_PLAYBOOK_CMD}" playbooks/upgrade.yml 

echo "Instances upgraded."

############################
##### BACKOFFICE WEBAPP ####
############################

echo "Installing Backoffice web application ..."

"${ANSIBLE_PLAYBOOK_CMD}" playbooks/deploy-backoffice.yml

echo "Backoffice application deployed."
