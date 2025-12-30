import os
from flask import Flask, jsonify
import redis

redis_client = redis.Redis(
    host=os.getenv("REDIS_HOST", "redis"),
    port=int(os.getenv("REDIS_PORT", "6379")),
    db=int(os.getenv("REDIS_DB", "0")),
    password=os.getenv("REDIS_PASSWORD"),
    decode_responses=True,
)

app = Flask(__name__)

@app.route("/")
def index():
    count = redis_client.incr("hits")
    return jsonify(visit_count=count, message="Hello"), 200

@app.route("/health")
def health():
    try:
        redis_client.ping()
        return jsonify(status="ok"), 200
    except redis.RedisError as exc:
        return jsonify(status="failed", detail=str(exc)), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
