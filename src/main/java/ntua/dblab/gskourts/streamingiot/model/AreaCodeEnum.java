package ntua.dblab.gskourts.streamingiot.model;

public enum AreaCodeEnum {
    ATHENS("ATH"),
    THESSALONIKI("THS"),
    PATRA("PAT"),
    KALAMATA("KAL"),
    KERKYRA("KER"),
    KOS("KOS"),
    LARISA("LAR"),
    LIMNOS("LIM"),
    LIVADEIA("LIV"),
    LOUTRAKI("LOU"),
    LAMIA("LAM");

    private final String areaCode;

    AreaCodeEnum(String areaCode) {
        this.areaCode = areaCode;
    }
}
