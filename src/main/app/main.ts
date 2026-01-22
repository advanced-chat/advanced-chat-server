import './assets/style.css'
import { RxStomp } from "@stomp/rx-stomp";

const source = new EventSource('/ui')

source.addEventListener('ac:configure-chat-service', event => {
    const rxStomp = new RxStomp();
    rxStomp.configure({
        brokerURL: 'ws://localhost:8080/rtm',
    });

    rxStomp.activate();

    const subscription = rxStomp
        .watch({ destination: "/application/user.relay" })
        .subscribe((message) => console.log(message.body));

    console.log(event)
})
