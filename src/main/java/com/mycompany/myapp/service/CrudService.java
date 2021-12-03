package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.enumeration.HUMO;
import com.mycompany.myapp.repository.HumoCardRepository;
import com.mycompany.myapp.service.dto.HumoCardDTO;
import com.mycompany.myapp.service.dto.HumoClientDTO;
import com.mycompany.myapp.service.mq.ElmaToAdmin;
import com.mycompany.myapp.service.mq.ElmaToHumo;
import com.mycompany.myapp.service.mq.dto.LoanDTO;
import com.mycompany.myapp.service.mq.dto.RequestDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

@Service
public class CrudService {


    private final HumoClientService humoClientService;
    private final HumoCardService humoCardService;
    private final HumoCardRepository humoCardRepository;
    //    private Set<Card> cardss;
    private final RabbitTemplate template;

    public CrudService(HumoClientService humoClientService, HumoCardService humoCardService, HumoCardRepository humoCardRepository, RabbitTemplate template) {
        this.humoClientService = humoClientService;
        this.humoCardService = humoCardService;
        this.humoCardRepository = humoCardRepository;
        this.template = template;
    }



    public Mono<?> createCrud(){
        HumoClientDTO humoClientDTO = new HumoClientDTO();
        humoClientDTO.setAddress("adrress " + String.format("%05d", new SecureRandom().nextInt(10000)));
        humoClientDTO.setName("name " + String.format("%05d", new SecureRandom().nextInt(10000)));
        humoClientDTO.setPhone(String.format("%05d", new SecureRandom().nextInt(10000)));

        return humoClientService.save(humoClientDTO).flatMap(client -> {
            String cardNumber = "cardNumber: " + String.format("%05d", new SecureRandom().nextInt(100000));
            String bankNumber =  String.format("%05d", new SecureRandom().nextInt(100));
            String sysNumber = "cardNumber: " + String.format("%05d", new SecureRandom().nextInt(100000));
            HumoCardDTO humoCardDTO = new HumoCardDTO();
            humoCardDTO.setCardNumber(cardNumber);
            humoCardDTO.setHumoClient(client);
            humoCardDTO.setBalance(new BigDecimal(String.format("%05d", new SecureRandom().nextInt(100000))));
            humoCardDTO.setCredit(true);
            humoCardDTO.setCardType(HUMO.HUMO);
            humoCardDTO.setBankNumber(bankNumber);
            humoCardDTO.setSysNumber(sysNumber);
            humoCardDTO.setExpireDate(new Date());
            humoCardDTO.setPan(cardNumber+bankNumber+sysNumber);
            humoCardDTO.setMaskedPan(cardNumber+ RandomStringUtils.random(5) + sysNumber);
            LoanDTO loanDTO = new LoanDTO( client.getId(), new BigDecimal(String.format("%05d", new SecureRandom().nextInt(100000))), new Date(), new Date());
            template.convertAndSend(ElmaToAdmin.EXCHANGE, ElmaToAdmin.ROUTING_KEY, loanDTO);
            return humoCardService.save(humoCardDTO).flatMap(card -> {
                humoClientDTO.setId(card.getHumoClient().getId());
                card.setHumoClient(humoClientDTO);
//                Map<String, Object> map = new ObjectMapper().convertValue(card, Map.class);
                template.convertAndSend(ElmaToHumo.EXCHANGE, ElmaToHumo.ROUTING_KEY, card);
                return Mono.just(card);
            });
        });
    }


    public Mono<?> sendRabbit(Object message) {
       template.convertAndSend(ElmaToHumo.EXCHANGE, ElmaToHumo.ROUTING_KEY, message);
      return Mono.just(message);
    }

    public Flux<?> save1(RequestDTO requestDTO) {
        Flux<Integer> flux = Flux.range(0, Integer.parseInt(requestDTO.getQuantity()) );

        return flux.flatMap(integer -> {
            return createCrud();
        });
//        return Mono.just("test");
    }

//    public Mono<Void> writeOffMoney(String pan){
//        return humoCardRepository.findByPan(pan).flatMap(humoCard -> {
//            humoCard.setBalance(humoCard.getBalance().subtract());
//            return humoCardRepository.save()
//        })
//    }

//    private Mono<?> save() {
//        Client client = new Client();
//        client.setName("name " + String.format("%05d", new SecureRandom().nextInt(10000)));
//        client.setAddress("adrress " + String.format("%05d", new SecureRandom().nextInt(10000)));
//        client.setPhone(String.format("%05d", new SecureRandom().nextInt(10000)));
////        client.setClients(cards());
//        return clientRepository.save(client).flatMap(client1 -> {
//            Set<Card> cards = new HashSet<>();
//            Card card = new Card();
//            card.setBalance(new BigDecimal(String.format("%05d", new SecureRandom().nextInt(100000))));
//            card.setBankNumber("60");
//            card.setCredit(true);
//            card.setSysNumber("01");
//            card.setExpireDate(LocalDate.now());
//            card.setTypeCard(CardType.HUMO);
//            card.setCard(client1);
//            card.setCardNumber("cardNumber: " + String.format("%05d", new SecureRandom().nextInt(100000)));
//            cards.add(card);
//            return cardRepository.save(card).flatMap(card1 -> clientRepository.findById(client1.getId()).flatMap(client2 -> {
//                client2.setClients(cards);
//                return clientRepository.save(client2).flatMap(client3 -> {
//                    ClientDTO clientDTO = new ClientDTO();
//                    clientDTO.setAddress(client3.getAddress());
//                    clientDTO.setName(client3.getName());
//                    clientDTO.setPhone(client3.getPhone());
//                    Set<CardDTO> cardDTOS = new HashSet<>();
//                    CardDTO cardDTO = new CardDTO();
//                    cardDTO.setCardId(card.getCardId());
//                    cardDTO.setCardNumber(card.getCardNumber());
//                    cardDTO.setCard(clientDTO);
//                    cardDTO.setBalance(card.getBalance());
//                    cardDTO.setTypeCard(card.getTypeCard());
//                    cardDTO.setCredit(card.getCredit());
//                    cardDTO.setBankNumber(card.getBankNumber());
//                    cardDTO.setSysNumber(card.getSysNumber());
//                    Date date = Date.from(card.getExpireDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
//                    cardDTO.setExpireDate(date);
//                    cardDTOS.add(cardDTO);
//                    clientDTO.setClients(cardDTOS);
//                   template.convertAndSend(ElmaToHumo.EXCHANGE, ElmaToHumo.ROUTING_KEY, clientDTO);
//                   return Mono.just(client3);
//                });
//            }));
//        });
//    }

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
