package za.ac.cput.domain;

import java.time.LocalDate;

public class WasteCollectionRequest {
    private String requestId;
    private LocalDate requestDate;
    ;
    private RequestStatus requestStatus;
    private User resident;
    private PickUpSchedule schedule;
    private Payment payment;
    private WasteCollectorProfile collector;

    private WasteCollectionRequest(Builder builder) {
        this.requestId = builder.requestId;
        this.requestDate = builder.requestDate;
        this.requestStatus = builder.requestStatus;
        this.resident = builder.resident;
        this.schedule = builder.schedule;
        this.payment = builder.payment;
        this.collector = builder.collector;
        this.requestDate = builder.requestDate;
    }

    public void assignCollector(WasteCollectorProfile collector) {
        this.collector = collector; }
    //public void updateStatus(RequestStatus newStatus) {
       // this.status = newStatus; }
    //public double calculatePayment() {
       // return (collector != null && collector.isPrivateCollector()) ? collector.getPricePerPickup() : 0; }
    public boolean isPickupScheduled() {
        return schedule != null; }


    public String getRequestId() {
        return requestId;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public User getResident() {
        return resident;
    }

    public PickUpSchedule getSchedule() {
        return schedule;
    }

    public Payment getPayment() {
        return payment;
    }

    public WasteCollectorProfile getCollector() {
        return collector;
    }
    public static class Builder{
        private String requestId;
        private LocalDate requestDate;
        private RequestStatus requestStatus;
        private User resident;
        private PickUpSchedule schedule;
        private Payment payment;
        private WasteCollectorProfile collector;
        public Builder setRequestId(String requestId) {
            this.requestId = requestId;
            return this;
        }
        public Builder setRequestDate(LocalDate requestDate) {
            this.requestDate = requestDate;
            return this;

        }
        public Builder setRequestStatus(RequestStatus requestStatus) {
            this.requestStatus = requestStatus;
            return this;
        }
        public Builder setResident(User resident) {
            this.resident = resident;
            return this;
        }
        public Builder setSchedule(PickUpSchedule schedule) {
            this.schedule = schedule;
            return this;
        }
        public Builder setPayment(Payment payment) {
            this.payment = payment;
            return this;
        }
        public Builder setCollector(WasteCollectorProfile collector) {
            this.collector = collector;
            return this;
        }
        public WasteCollectionRequest build(){
            return new WasteCollectionRequest(this);
        }
    }
}
