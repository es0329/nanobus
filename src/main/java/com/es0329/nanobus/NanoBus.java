package com.es0329.nanobus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class NanoBus {
    private static final boolean ENABLE_LOGGING = false;

    private static final Map<String, ArrayList<Subscriber>> map =
            Collections.synchronizedMap(new IdentityHashMap<String, ArrayList<Subscriber>>());

    public static void subscribe(String eventName, Subscriber newSubscriber) {
        if (!map.containsKey(eventName)) {
            map.put(eventName, new ArrayList<Subscriber>());
        }

        ArrayList<Subscriber> subscribers = map.get(eventName);

        if (!subscribers.contains(newSubscriber)) {
            subscribers.add(newSubscriber);
            int hashCode = newSubscriber.hashCode();
            log("#subscribe: #" + hashCode + " " + newSubscriber.getClass().getSimpleName() + " -> " + eventName);
        }
    }

    public static void unsubscribe(Subscriber unSubscriber) {
        for (HashMap.Entry<String, ArrayList<Subscriber>> entry : map.entrySet()) {

            if (entry.getValue().contains(unSubscriber)) {
                entry.getValue().remove(unSubscriber);
                log("#unsubscribe: " + unSubscriber.getClass().getSimpleName());
            }
        }
    }

    public static void publish(String eventName, Object object) {

        if (map.get(eventName) != null) {

            ArrayList<Subscriber> subscribers = new ArrayList<>(map.get(eventName));

            for (Subscriber s : subscribers) {
                if (s == null) {
                    log("#publish: " + eventName);
                    continue;
                }
                s.onEventReceived(eventName, object);
                log("#publish: " + eventName);
            }
        }
    }

    public static boolean returnTrue() {
        return true;
    }

    private static void log(String message) {
        if (ENABLE_LOGGING) /* println(message) */;
    }

    public interface Subscriber {
        void registerEvents();

        void onEventReceived(String name, Object object);
    }
}
