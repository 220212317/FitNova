package za.ac.cput.domain;

/* Payment entity
   Author: Athi (220212317)
   Date: 16 March 2026
*/

public class Payment {
    private String paymentId;
    private String userId;
    private double amount;
    private String paymentMethod;
    private String paymentDate;

    private Payment() {

    }


    private Payment(Builder builder) {
        this.paymentId = builder.paymentId;
        this.userId = builder.userId;
        this.amount = builder.amount;
        this.paymentMethod = builder.paymentMethod;
        this.paymentDate = builder.paymentDate;
    }


    public String getPaymentId() {
        return paymentId;
    }
    public String getUserId() {
        return userId;
    }
    public double getAmount() {
        return amount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public String getPaymentDate() {
        return paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                '}';
    }


    public static class Builder {
        private String paymentId;
        private String userId;
        private double amount;
        private String paymentMethod;
        private String paymentDate;

        public Builder setPaymentId(String paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
}
