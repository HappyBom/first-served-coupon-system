import random
from locust import HttpUser, task

class issue_V1(HttpUser):
    connection_timeout = 10.0
    network_timeout = 10.0

    @task
    def issue(self):
        payload = {
            "userId" : random.randint(1, 10000000),
            "couponId" : 1
        }
        response = self.client.post("/v1/issue-async", json=payload)

        if response.status_code != 200:
            print("요청 실패: {response.status_code}, 응답: {response.text}")

        #터미널 : docker-compose logs -f,
