import java.awt.*;

public class CreateBillboardsRequest {
    private String billboardAuthorName;
    private String billboardName;
    private String  billboardBackground;
    private String billboardMessageText;
    private String billboardMessageColour;
    private String billboardInfoText;
    private String billboardInfoColor;
    private String billboardPictureUrl;
    private String billboardPictureData;
    private String sessionToken;

    public CreateBillboardsRequest (String billboardAuthorName, String billboardName,
                                    String  billboardBackground, String billboardMessageText,
                                    String billboardMessageColour, String billboardPictureUrl,
                                    String billboardPictureData, String billboardInfoText,
                                    String billboardInfoColor) {

        this.billboardName = billboardName;
        this.billboardAuthorName = billboardAuthorName;
        this.billboardBackground = billboardBackground;
        this.billboardMessageText = billboardMessageText;
        this.billboardMessageColour = billboardMessageColour;
        this.billboardInfoText = billboardInfoText;
        this.billboardInfoColor = billboardInfoColor;
        this.billboardPictureUrl = billboardPictureUrl;
        this.billboardPictureData = billboardPictureData;
    }

    public String getBillboardAuthorName() { return billboardAuthorName; }

    public String getBillboardName() { return billboardName; }

    public String getBillboardBackground() { return billboardBackground; }

    public String getBillboardMessageText() { return billboardMessageText; }

    public String getBillboardMessageColour() { return billboardMessageColour; }

    public String getBillboardInfoText() { return billboardInfoText; }

    public String getBillboardInfoColor() { return billboardInfoColor; }

    public String getBillboardPictureUrl() { return billboardPictureUrl; }

    public String getBillboardPictureData() { return billboardPictureData; }

}
