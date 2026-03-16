package za.ac.cput.factory;

import za.ac.cput.domain.Payment;

public class PaymentFactory {
    public static Payment createPayment(String paymentId, String userId, double amount, String paymentMethod, String paymentDate){
        if(paymentId.isEmpty() || paymentId == null)
            return null;

        if(userId.isEmpty() || userId == null)
            return null;

        if(paymentMethod.isEmpty() || paymentMethod == null)
            return null;

        if(paymentDate.isEmpty() || paymentDate == null)
            return null;

        return new Payment.Builder()
                .setPaymentId(paymentId)
                .setUserId(userId)
                .setAmount(amount)
                .setPaymentMethod(paymentMethod)
                .setPaymentDate(paymentDate)
                .build();
    }

}
