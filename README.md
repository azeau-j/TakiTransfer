[![CI/CD Pipeline](https://github.com/azeau-j/TakiTransfer/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/azeau-j/TakiTransfer/actions/workflows/ci-cd.yml)
# TakiTransfer

TakiTransfer is an open-source, self-hostable alternative to popular file-sharing services like WeTransfer.
Designed with simplicity and ease of use in mind, TakiTransfer lets you securely transfer files between users via a web interface.
The project is fully containerized with Docker, allowing easy setup and deployment on your own infrastructure.

## Features

- **Self-hostable:** Run your own file transfer service without relying on third-party servers.
- **Dockerized:** Quick and simple installation via Docker, with a pre-configured docker-compose.yml file.
- **Web Interface:** Intuitive user-friendly web UI for uploading and downloading files.
- **No Account Required:** Simply upload and share a link to transfer files.
- **Multiple Storage Options:** Choose between different storage backends for storing uploaded files, such as the local filesystem or an S3-compatible bucket.
- **Extensible SDK:** Easily extend the project by adding your own storage providers using the provided SDK.

## Installation
### Requirements:

- Docker
- Docker Compose

## 🚀 Getting started:
For the moment, there is no images online, you will have to build them locally

### Clone the repository:

```shell
git clone https://github.com/yourusername/TakiTransfer.git
cd TakiTransfer
```
### Start the Docker containers:
The project includes a docker-compose.yml file for easy setup. To bring up the containers, run:

```shell
docker-compose up -d --build
```

### Access the Web UI:
Once the containers are running, you can access the TakiTransfer web interface by navigating to:

```
http://localhost:3000
```


## 🤝 Contributing
Check out the [Contributing Guide](CONTRIBUTING.md) for more information.

## License
TakiTransfer is licensed under the MIT License. See the [LICENSE](/LICENSE.md) file for more details.