Akka Cluster Example
===

#### Components
- Akka Cluster 2.5
- OpenShift 3.5
- SBT Native Packager 1.2

#### References
- http://doc.akka.io/docs/akka/current/scala/cluster-usage.html
- https://medium.com/google-cloud/clustering-akka-in-kubernetes-with-statefulset-and-deployment-459c0e05f2ea
- https://github.com/saturnism/akka-kubernetes-example

#### Developers

To quickly get up and running, do a 

`sbt docker:publishLocal && docker-compose rm -f && docker-compose up`

##### Connecting to the cluster

http://doc.akka.io/docs/akka/current/scala/cluster-client.html

##### Messaging within the cluster

http://doc.akka.io/docs/akka/current/scala/distributed-pub-sub.html

##### Cluster Management

Akka HTTP Management is configured

- http://developer.lightbend.com/docs/akka-management/current/cluster-http-management.html#preparing-your-project-for-cluster-http-management

Also included is the deprecated akka-cluster scripts

```
#! ./hack/akka-cluster help
This jmx-client/akka-cluster tool is deprecated use curl and https://github.com/akka/akka-cluster-management instead, since 2.5.0.
Usage: akka-cluster <node-hostname> <jmx-port> <optional: -p cluster-port> <command> ...

Supported commands are:
           join <node-url> - Sends request a JOIN node with the specified URL
          leave <node-url> - Sends a request for node with URL to LEAVE the cluster
           down <node-url> - Sends a request for marking node with URL as DOWN
             member-status - Asks the member node for its current status
                   members - Asks the cluster for addresses of current members
               unreachable - Asks the cluster for addresses of unreachable members
            cluster-status - Asks the cluster for its current status (member ring, unavailable nodes, meta data etc.)
                    leader - Asks the cluster who the current leader is
              is-singleton - Checks if the cluster is a singleton cluster (single node cluster)
              is-available - Checks if the member node is available
Where the <node-url> should be on the format of 'akka.tcp://actor-system-name@hostname:port'

Examples: ./akka-cluster localhost 9999 is-available
          ./akka-cluster localhost 9999 join akka.tcp://MySystem@darkstar:2552
          ./akka-cluster localhost 9999 cluster-status
          ./akka-cluster localhost 9999 -p 2551 is-available
          ./akka-cluster localhost 9999 -p 2551 join akka.tcp://MySystem@darkstar:2552
          ./akka-cluster localhost 9999 -p 2551 cluster-status
```
