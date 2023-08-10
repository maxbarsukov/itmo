import * as express from 'express';
import * as http from 'http';
import * as WebSocket from 'ws';
import * as amqp from 'amqplib';

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

const {
    RABBIT_USERNAME,
    RABBIT_PASSWORD,
    RABBIT_HOST,
    RABBIT_PORT,
} = process.env;

connectQueue();

async function connectQueue() {
    try {
        const uri = `amqp://${RABBIT_USERNAME}:${RABBIT_PASSWORD}@${RABBIT_HOST}:${RABBIT_PORT}/`;
        const connection = await amqp.connect(uri);
        const channel = await connection.createChannel();
        await channel.assertQueue("products");

        await channel.consume("products", data => {
            if (data === null) return;

            const json = data.content.toString('utf-8');
            wss.clients.forEach(client => client.send(json));
            channel.ack(data);
        });
    } catch (error) {
        console.error(error);
    }
}

server.listen(process.env.PORT || 8081, () => {
    console.log(`Server started on port ${process.env.PORT || 8081}`);
});
