package interstore.FunctionSetAssignments;

import interstore.Identity.Link;
import interstore.Identity.ListLink;
import interstore.Identity.Resource;
import interstore.Time.TimeDto;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionSetAssignmentBase extends Resource {
    String TimeLink;
    ListLink DemandResponseProgramListLink = new ListLink();
    ListLink TariffProfileListLink = new ListLink();
    ListLink MessagingProgramListLink = new ListLink();
    ListLink FileListLink = new ListLink();
    ListLink UsagePointListLink = new ListLink();
    ListLink DERProgramListLink= new ListLink();
    ListLink CustomerAccountListLink = new ListLink();
    ListLink PrepaymentListLink = new ListLink();
    ListLink ResponseSetListLink = new ListLink();
    public static HashMap<String, String> timeMap = new HashMap<>();
    public static HashMap<String, ArrayList<String>> timeDetailsMap = new HashMap<>();



    private Link link = new Link();
    public static TimeDto time;

    public FunctionSetAssignmentBase(String href){
        super(href);
    }

    public FunctionSetAssignmentBase(){
        ArrayList<String> timeDetails = new ArrayList<>();
//        setTime();


//        if(timeMap.get(this.TimeLink)== null){
//            timeMap.put(this.TimeLink, this.time.toString());
//            timeDetails.add(String.valueOf(this.time.getCurrentTime()));
//            timeDetails.add(this.time.getQuality());
//            timeDetailsMap.put(this.time.toString(), timeDetails);
//        }
    }

//    @Transactional
//    public void setTime(){
//        link.setLink("/tm");
//        this.TimeLink = link.getLink();
//        this.time = new TimeDto();
//        this.time.setTimeLink(link.getLink());
//        timeDtoRepository.save(this.time);
//    }


    public void updateCurrentTime(String updatedTime) {
        this.time.setCurrentTime(updatedTime);
        ArrayList<String> timeDetails = timeDetailsMap.get(timeMap.get(this.TimeLink));
        if (timeDetails != null && !timeDetails.isEmpty()){
            timeDetails.set(0, String.valueOf(this.time.getCurrentTime()));
            timeDetailsMap.put(timeMap.get(this.TimeLink), timeDetails);
        }
    }

    // Setters
    public void setTimeLink(String TimeLink) {
        this.TimeLink = TimeLink;
    }

    public void setDemandResponseProgramListLink(ListLink DemandResponseProgramListLink) {
        this.DemandResponseProgramListLink = DemandResponseProgramListLink;
    }

    public void setTariffProfileListLink(ListLink TariffProfileListLink) {
        this.TariffProfileListLink = TariffProfileListLink;
    }

    public void setMessagingProgramListLink(ListLink MessagingProgramListLink) {
        this.MessagingProgramListLink = MessagingProgramListLink;
    }

    public void setFileListLink(ListLink FileListLink) {
        this.FileListLink = FileListLink;
    }

    public void setUsagePointListLink(ListLink UsagePointListLink) {
        this.UsagePointListLink = UsagePointListLink;
    }

    public void setDERProgramListLink(ListLink DERProgramListLink) {
        this.DERProgramListLink = DERProgramListLink;
    }

    public void setCustomerAccountListLink(ListLink CustomerAccountListLink) {
        this.CustomerAccountListLink = CustomerAccountListLink;
    }

    public void setPrepaymentListLink(ListLink PrepaymentListLink) {
        this.PrepaymentListLink = PrepaymentListLink;
    }

    public void setResponseSetListLink(ListLink ResponseSetListLink) {
        this.ResponseSetListLink = ResponseSetListLink;
    }

    // Getters
    public String getTimeLink() {
        return TimeLink;
    }

    public ListLink getDemandResponseProgramListLink() {
        return DemandResponseProgramListLink;
    }

    public ListLink getTariffProfileListLink() {
        return TariffProfileListLink;
    }

    public ListLink getMessagingProgramListLink() {
        return MessagingProgramListLink;
    }

    public ListLink getFileListLink() {
        return FileListLink;
    }

    public ListLink getUsagePointListLink() {
        return UsagePointListLink;
    }

//    public ListLink getDERProgramListLink() {
//        return DERProgramListLink;
//    }

    public ListLink getCustomerAccountListLink() {
        return CustomerAccountListLink;
    }

    public ListLink getPrepaymentListLink() {
        return PrepaymentListLink;
    }

    public ListLink getResponseSetListLink() {
        return ResponseSetListLink;
    }

}
