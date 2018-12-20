package fr.insat.om2m.tp2.test;

import obix.Int;
import obix.Obj;
import obix.Str;
import obix.io.ObixEncoder;
import org.eclipse.om2m.commons.resource.*;

import fr.insat.om2m.tp2.mapper.Mapper;
import fr.insat.om2m.tp2.mapper.MapperInterface;
import fr.insat.om2m.tp2.client.Response;
import fr.insat.om2m.tp2.util.RequestLoader;
import fr.insat.om2m.tp2.client.Client;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Monitor {

    public static void main(String[] args) throws IOException, InterruptedException {

        Client cl = new Client();
        Response res;
        MapperInterface mapper = new Mapper();

        // Créer AE; CNT
        AE ae = new AE();
        ae.setAppName("app-luxmeter");
        ae.setAppID("app-luxmeter");
        ae.setName("app-luxmeter");
        ae.setRequestReachability(true);
        res = cl.create("http://localhost:8080/~/in-cse", mapper.marshal(ae), "admin:admin", "2");

        Container cnt = new Container();
        cnt.setName("Mesures");
        res = cl.create("http://localhost:8080/~/in-cse/in-name/app-luxmeter", mapper.marshal(cnt), "admin:admin", "3");

        // Souscrire au CNT
        Subscription sub = new Subscription();
        sub.setName("subscription-value");
        sub.getNotificationURI().add("http://localhost:5647/monitor");
        sub.setNotificationContentType(new BigInteger("2")); // pose pas de question c'est 2

        res = cl.create("http://localhost:8080/~/in-cse/in-name/app-luxmeter/Mesures", mapper.marshal(sub), "admin:admin", "23");
        System.out.println(res.getStatusCode());

        // Boucle infinie
        // Pousser valeurs par CIN
        while (true) {
            TimeUnit.SECONDS.sleep(5);

            ContentInstance cin = new ContentInstance();
            int randomNum = ThreadLocalRandom.current().nextInt(0, 600 + 1);
            Int value = new Int(randomNum);
            Str location = new Str("Salle à manger");
            Obj payload = new Obj();
            payload.add("luminosity", value);
            payload.add("location", location);
            cin.setContent(ObixEncoder.toString(payload));
            res = cl.create("http://localhost:8080/~/in-cse/in-name/app-luxmeter/Mesures", mapper.marshal(cin), "admin:admin", "4");

            System.out.println("Pushed value " + randomNum + ", status code " + res.getStatusCode());
        }
    }


}
