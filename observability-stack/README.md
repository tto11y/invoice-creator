# Local Grafana Observability Stack

The [Docker compose file](docker-compose.yml) contains a fully functioning Grafana stack including ...
1. Loki (logs backend)
2. Grafana (observability frontend)
3. Tempo (traces backend)
4. Prometheus (metrics backend)
5. Alloy (Grafana's OpenTelemetry collector)

For the Alloy container to function properly the files [config.alloy](alloy/config.alloy) and [endpoints.json](alloy/endpoints.json) are required.

For the Tempo container to function properly the file [tempo.yaml](tempo-distributed/tempo.yaml) is required.

## Running the stack

To run the Grafana observability stack, execute the following command in the `observability-stack` directory:

```bash
  docker-compose up -d
  # or docker compose up -d
```

```bash
  # or if you use Podman:
  
  # in case the podman machine is not yet initialized, execute:
  podman machine init
  
  # in case the podman machine is not yet running, execute:
  podman machine start
  
  podman compose up -d
```

This command will start all the necessary services in detached mode.

## Stopping the stack

To stop the Grafana observability stack, execute the following command in the `observability-stack` directory:

```bash
  docker-compose down
  # or docker compose down
```

```bash
  # or if you use Podman:
  podman compose down
```

## Accessing Grafana

Once the stack is running, you can access the Grafana dashboard by navigating to `http://localhost:3000` in your web browser.

## Accessing Alloy

You can access the Alloy OpenTelemetry collector debug frontend by navigating to `http://localhost:12345` in your web browser.

## Verifying data ingestion

To verify that your local observability stack is set up correctly and receiving data, you can use the command line tool [telemetrygen](https://pkg.go.dev/github.com/open-telemetry/opentelemetry-collector-contrib/cmd/telemetrygen).

### Setup telemetrygen

#### Install Go (if not already installed)

You can do it either by following the official instructions at https://golang.org/doc/install or using a package manager:

```bash
  # On macOS using Homebrew
  brew install go
```

```bash
  # On Ubuntu/Debian
  sudo apt update
  sudo apt install golang-go
```

#### Install telemetrygen

```bash
  go install github.com/open-telemetry/opentelemetry-collector-contrib/cmd/telemetrygen@latest
```

### Logs

```bash
  telemetrygen logs --duration 5s --otlp-insecure
```

### Traces

```bash
  telemetrygen traces --duration 5s --otlp-insecure
```

### Metrics

```bash
  # by default, the metric name is 'gen'
  # you can specify a different name using the flag --otlp-metric-name
  telemetrygen metrics --duration 5s --otlp-insecure
```