package com.mycompany.myapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.domain.Card;
import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.domain.enumeration.CardType;
import com.mycompany.myapp.domain.enumeration.HUMO;
import com.mycompany.myapp.repository.CardRepository;
import com.mycompany.myapp.repository.ClientRepository;
import com.mycompany.myapp.service.dto.CardDTO;
import com.mycompany.myapp.service.dto.ClientDTO;
import com.mycompany.myapp.service.dto.HumoCardDTO;
import com.mycompany.myapp.service.dto.HumoClientDTO;
import com.mycompany.myapp.service.mq.ElmaToHumo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class CrudService {

    private final CardRepository cardRepository;
    private final ClientRepository clientRepository;
    private final HumoClientService humoClientService;
    private final HumoCardService humoCardService;
    //    private Set<Card> cardss;
    private final RabbitTemplate template;

    public CrudService(CardRepository cardRepository, ClientRepository clientRepository, HumoClientService humoClientService, HumoCardService humoCardService, RabbitTemplate template) {
        this.cardRepository = cardRepository;
        this.clientRepository = clientRepository;
        this.humoClientService = humoClientService;
        this.humoCardService = humoCardService;
        this.template = template;
    }



    public Mono<?> createCrud(){
        HumoClientDTO humoClientDTO = new HumoClientDTO();
        humoClientDTO.setAddress("adrress " + String.format("%05d", new SecureRandom().nextInt(10000)));
        humoClientDTO.setName("name " + String.format("%05d", new SecureRandom().nextInt(10000)));
        humoClientDTO.setPhone(String.format("%05d", new SecureRandom().nextInt(10000)));

        return humoClientService.save(humoClientDTO).flatMap(client -> {
            HumoCardDTO humoCardDTO = new HumoCardDTO();
            humoCardDTO.setCardNumber("cardNumber: " + String.format("%05d", new SecureRandom().nextInt(100000)));
            humoCardDTO.setHumoClient(client);
            humoCardDTO.setBalance(new BigDecimal(String.format("%05d", new SecureRandom().nextInt(100000))));
            humoCardDTO.setCredit(true);
            humoCardDTO.setCardType(HUMO.HUMO);
            humoCardDTO.setBankNumber("60");
            humoCardDTO.setCardNumber("cardNumber: " + String.format("%05d", new SecureRandom().nextInt(100000)));
            humoCardDTO.setExpireDate(new Date());
//            humoCardDTO.setExpireDate(LocalDate.now());
            humoCardDTO.setSysNumber("01");
            return humoCardService.save(humoCardDTO).flatMap(card -> {
                humoClientDTO.setId(card.getHumoClient().getId());
                card.setHumoClient(humoClientDTO);
//                Map<String, Object> map = new ObjectMapper().convertValue(card, Map.class);
                template.convertAndSend(ElmaToHumo.EXCHANGE, ElmaToHumo.ROUTING_KEY, card);
                return Mono.just(card);
            });
        });
    }


    public Mono<Void> sendRabbit(Object message) {
       template.convertAndSend(ElmaToHumo.EXCHANGE, ElmaToHumo.ROUTING_KEY, message);
      return Mono.empty();
    }

    public Flux<?> save1() {
        Flux<Integer> flux = Flux.range(0, 3);

        return flux.flatMap(integer -> {
            return save();
        });
//        return Mono.just("test");
    }

    private Mono<?> save() {
        Client client = new Client();
        client.setName("name " + String.format("%05d", new SecureRandom().nextInt(10000)));
        client.setAddress("adrress " + String.format("%05d", new SecureRandom().nextInt(10000)));
        client.setPhone(String.format("%05d", new SecureRandom().nextInt(10000)));
//        client.setClients(cards());
        return clientRepository.save(client).flatMap(client1 -> {
            Set<Card> cards = new HashSet<>();
            Card card = new Card();
            card.setBalance(new BigDecimal(String.format("%05d", new SecureRandom().nextInt(100000))));
            card.setBankNumber("60");
            card.setCredit(true);
            card.setSysNumber("01");
            card.setExpireDate(LocalDate.now());
            card.setTypeCard(CardType.HUMO);
            card.setCard(client1);
            card.setCardNumber("cardNumber: " + String.format("%05d", new SecureRandom().nextInt(100000)));
            cards.add(card);
            return cardRepository.save(card).flatMap(card1 -> clientRepository.findById(client1.getId()).flatMap(client2 -> {
                client2.setClients(cards);
                return clientRepository.save(client2).flatMap(client3 -> {
                    ClientDTO clientDTO = new ClientDTO();
                    clientDTO.setAddress(client3.getAddress());
                    clientDTO.setName(client3.getName());
                    clientDTO.setPhone(client3.getPhone());
                    Set<CardDTO> cardDTOS = new HashSet<>();
                    CardDTO cardDTO = new CardDTO();
                    cardDTO.setCardId(card.getCardId());
                    cardDTO.setCardNumber(card.getCardNumber());
                    cardDTO.setCard(clientDTO);
                    cardDTO.setBalance(card.getBalance());
                    cardDTO.setTypeCard(card.getTypeCard());
                    cardDTO.setCredit(card.getCredit());
                    cardDTO.setBankNumber(card.getBankNumber());
                    cardDTO.setSysNumber(card.getSysNumber());
                    Date date = Date.from(card.getExpireDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    cardDTO.setExpireDate(date);
                    cardDTOS.add(cardDTO);
                    clientDTO.setClients(cardDTOS);
                   template.convertAndSend(ElmaToHumo.EXCHANGE, ElmaToHumo.ROUTING_KEY, clientDTO);
                   return Mono.just(client3);
                });
            }));
        });
    }

//    private Mono<Client> save(Client client) {
//        return clientRepository.save(client);
//    }
//
//    private Mono<Set<Card>> cards() {
//        Set<Card> cards = new HashSet<>();
//        for (int i = 0; i < 5; i++) {
//            Card card = new Card();
//            card.setBalance(new BigDecimal(String.format("%05d", new SecureRandom().nextInt(100000))));
//            card.setBankNumber("60");
//            card.setCredit(true);
//            card.setSysNumber("01");
//            card.setExpireDate(LocalDate.now());
//            card.setTypeCard(CardType.HUMO);
//            cards.add(card);
//            saveCard(card);
//        }
//        return cards;
//    }
//
//    private Mono<Card> saveCard(Card card) {
//        return cardRepository.save(card);
//    }
}
