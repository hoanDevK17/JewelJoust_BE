    package online.jeweljoust.BE.service;

    import jakarta.transaction.Transactional;
    import online.jeweljoust.BE.entity.*;
    import online.jeweljoust.BE.enums.AuctionBidStatus;
    import online.jeweljoust.BE.enums.AuctionRegistrationStatus;
    import online.jeweljoust.BE.enums.AuctionSessionStatus;
    import online.jeweljoust.BE.enums.TransactionType;
    import online.jeweljoust.BE.model.AuctionBidRequest;
    import online.jeweljoust.BE.model.AuctionRegistrationRequest;
    import online.jeweljoust.BE.respository.AuctionBidRepository;
    import online.jeweljoust.BE.respository.AuctionRegistrationRepository;
    import online.jeweljoust.BE.respository.AuctionSessionRepository;
    import online.jeweljoust.BE.respository.WalletRepository;
    import online.jeweljoust.BE.utils.AccountUtils;
    import org.checkerframework.checker.units.qual.A;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.stereotype.Service;

    import java.util.Date;
    import java.util.List;
    import java.util.Optional;

    @Service
    public class AuctionBidService {
        @Autowired
        AuctionRegistrationRepository auctionRegistration;
        @Autowired
        AuctionBidRepository auctionBidRepository;
        @Autowired
        AuctionSessionRepository auctionSessionRepository;
        @Autowired
        AuctionRegistrationRepository auctionRegistrationRepository;
        @Autowired
        WalletService walletService;
        @Autowired
        AccountUtils accountUtils;

        @Autowired
        SimpMessagingTemplate messagingTemplate;

        @Transactional
        public AuctionBid addAuctionBid(AuctionBidRequest auctionBidRequest) {


            AuctionSession auctionSession = auctionSessionRepository.findAuctionSessionById(auctionBidRequest.getId_session());
            if(!auctionSession.getStatus().equals(AuctionSessionStatus.BIDDING)){
                throw new
                        IllegalStateException("Cannot bid for this session. The session is not in the bidding phase.");
            }

            AuctionBid highestBid = auctionBidRepository.findHighestBidBySessionId(auctionSession.getId()).orElse(new AuctionBid());

            if(auctionBidRequest.getPrice() <= highestBid.getBid_price() ) {
                throw new IllegalStateException("Bid amount must be higher than the current highest bid");
            }   
            AuctionBid highestBidOfUser = auctionBidRepository.findHighestBidByUserAndSessionAndStatus(accountUtils.getAccountCurrent().getId(),auctionSession.getId()).orElse(new AuctionBid());



            double price = auctionBidRequest.getPrice() - highestBidOfUser.getBid_price();

            AuctionBid auctionBid = this.handleNewBidTransaction(auctionBidRequest.getPrice(),highestBidOfUser.getAuctionRegistration().getId());
            walletService.changBalance(accountUtils.getAccountCurrent().getWallet().getId(), -price,TransactionType.BIDDING,"Bidding" + price );
            highestBidOfUser.setStatus(AuctionBidStatus.NONACTIVE);
            auctionBidRepository.save(highestBidOfUser);
            AuctionBid newauctionBid= auctionBidRepository.save(auctionBid);
            messagingTemplate.convertAndSend("/topic/JewelJoust","addBid");
            return newauctionBid;



    //walletService
        }
        @Transactional
        public AuctionBid handleNewBidTransaction(double price,long registration_id){
            AuctionBid auctionBid = new AuctionBid();
            auctionBid.setAuctionRegistration(auctionRegistrationRepository.findAuctionRegistrationById(registration_id));
            auctionBid.setBid_price(price);
            auctionBid.setBid_time(new Date());
            auctionBid.setStatus(AuctionBidStatus.ACTIVE);
            return auctionBid;
        }
//        public List<AuctionBid> getAllBidding(){
//           auctionBidRepository.findHighestBidBySessionId()
//            return auctionBid;
//        }
    }
