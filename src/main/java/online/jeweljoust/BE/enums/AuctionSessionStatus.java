package online.jeweljoust.BE.enums;

    public enum AuctionSessionStatus {
        CREATED("created"),
        STARTED("started"),
        PAUSED("paused"),
        RESUMED("resumed"),
        ENDED("ended"),
        CANCELLED("cancelled"),
        FINALIZED("finalized");

        private final String value;

        AuctionSessionStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }


}
