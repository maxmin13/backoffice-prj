# AWS backoffice project

The project deploys a Java web application to AWS.</br>
The web application runs on Tomacat HTTP server backed by MariaDB database.</br>

## Local work machine requirements: ##

- Fedora 40
- Maven 3.9.5
- Java 21
- Python 3.12.7
- docker-compose (Python library) 1.29.2
- Ansible 9.11.0
- Docker 27.4.1

## Configure the AWS credentials and default region in your work machine: ##

Log into AWS management console as root, go to IAM, Users, create a new user.</br>
Select the user and create the access keys.</br>
Associate the user with an identity-based policy that allows access to route53 webservices, for ex: AmazonRoute53FullAccess.</br>
Associate the user with an identity-based policy that allows access to ec2 webservices, for ex: AmazonEC2FullAccess.</br>
The access key, the secret access key, the AWS region associated with the user have to be exported before running the scripts.


## Clone the project: ##

git clone git@github.com:maxmin13/backoffice-prj.git

## Configure the project: ##

edit **config/datacenter.json** and **config/hostedzone.json** and set the Vpc and DNS values according 
to your AWS account: <br>

* VPC CIDR (eg: "Cidr": "10.0.0.0/16")<br>
* VPC region (eg: "Region": "eu-west-1")<br>
* Availability zone (eg: "Az": "eu-west-1a")<br>
* Subnet CIDR (eg: "Cidr": "10.0.20.0/24")<br>
* Instance private IP (eg: "PrivateIp": "10.0.20.35")<br>
* (Not mandatory) DNS registered domain (your domain registered with the AWS registrar, eg: "RegisteredDomain": "maxmin.it")<br>


## Install the web application: ##

```
export AWS_ACCESS_KEY_ID=<AWS IAM user credentials>
export AWS_SECRET_ACCESS_KEY=<AWS IAM user credentials>
export AWS_DEFAULT_REGION=<AWS IAM user credentials>

cd backoffice-prj/bin
chmod +x make.sh

./make.sh
```

## ssh in the remote AWS instance: ##

```
// ssh into the remote instance, with the instance private key and the instance user:
cd backoffice-prj/access

rm -f ~/.ssh/known_hosts && ssh -v -i backoffice-box -p 22 backofficeadmin@backoffice.maxmin.it
```


## Access the database: ##

http://backoffice.maxmin.it:8000<br>
root<br>
mariadbrootsecret<br>


## Access the web site at: ##

*https://backoffice.maxmin.it:8443/home*


## Delete the datacenter and the application: ##

```
export AWS_ACCESS_KEY_ID=<AWS IAM user credentials>
export AWS_SECRET_ACCESS_KEY=<AWS IAM user credentials>
export AWS_DEFAULT_REGION=<AWS IAM user credentials>

cd backoffice-prj/bin
chmod +x delete.sh

./delete.sh

```

<br>
