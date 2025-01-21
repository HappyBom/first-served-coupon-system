from locust import HttpUser, task

class HelloWorld(HttpUser):
    connection_timeout = 10.0
    network_timeout = 10.0

    @task
    def hello_world(self):
        self.client.get("/hello")