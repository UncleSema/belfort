package ru.ct.belfort.client;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TinkoffClientService {
    private final Map<String, TinkoffClient> clients = new HashMap<>();
    public TinkoffClient getClientByToken(String token) {
        return clients.computeIfAbsent(token, TinkoffClient::new);
    }
    public void removeClientByToken(String token) {
        //assuming that we have that token in 'clients'
        clients.remove(token);
    }
}
