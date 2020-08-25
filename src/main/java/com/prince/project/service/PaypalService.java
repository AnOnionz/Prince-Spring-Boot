package com.prince.project.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Payout;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.api.payments.PayoutItem;
import com.paypal.api.payments.PayoutSenderBatchHeader;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.prince.project.config.PaypalConfig;
import com.prince.project.config.PaypalPaymentIntent;
import com.prince.project.config.PaypalPaymentMethod;
import com.prince.project.model.Post;

@Service
public class PaypalService {
	@Autowired 
	private PaypalConfig PaypalConfig;
	
	@Autowired
	private APIContext apiContext;
	
	public Payment createPayment(
			List<Post> listPost,
			String currency, 
			PaypalPaymentMethod method, 
			PaypalPaymentIntent intent, 
			String description, 
			String cancelUrl, 
			String successUrl) throws PayPalRESTException{
		Double total = 0.00;
		// ### Items
		List<Item> items = new ArrayList<Item>();
		ItemList itemList = new ItemList();
		for (Post onePost : listPost) {
			Item item = new Item();
			item.setName("Quảng cáo :"+ onePost.getTitleLimited(6)).setQuantity("1").setCurrency(currency).setPrice(""+onePost.getCost());
			total+=onePost.getCost();
			items.add(item);
		}
		itemList.setItems(items);
		Details details = new Details();
		details.setSubtotal("" + total);
		Amount amount = new Amount();
		amount.setCurrency(currency);
		amount.setTotal("" + total);
		amount.setDetails(details);

		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);
		transaction.setItemList(itemList);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(method.toString());

		Payment payment = new Payment();
		payment.setIntent(intent.toString());
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		payment.setRedirectUrls(redirectUrls);

		return payment.create(apiContext);
	}

	public PayoutBatch createPayout(String userEmail, String money, String currency) {
		// ###Payout
		// A resource representing a payout
		Payout payout = new Payout();
		
		PayoutSenderBatchHeader senderBatchHeader = new PayoutSenderBatchHeader();

		// ### NOTE:
		// You can prevent duplicate batches from being processed. If you
		// specify a `sender_batch_id` that was used in the last 30 days, the
		// batch will not be processed. For items, you can specify a
		// `sender_item_id`. If the value for the `sender_item_id` is a
		// duplicate of a payout item that was processed in the last 30 days,
		// the item will not be processed.
		// #### Batch Header Instance
		Random random = new Random();
		senderBatchHeader.setSenderBatchId(new Double(random.nextDouble()).toString())
				.setEmailSubject("Nhận tiền từ Prince!");

		// ### Currency
		Currency amount = new Currency();
		amount.setValue(money).setCurrency(currency);

		// #### Sender Item
		// Please note that if you are using single payout with sync mode, you
		// can only pass one Item in the request
		PayoutItem senderItem = new PayoutItem();
		senderItem.setRecipientType("Email").setNote("Phần thưởng xem quảng cáo của bạn!")
				.setReceiver(userEmail).setSenderItemId("202004324234").setAmount(amount);

		List<PayoutItem> items = new ArrayList<PayoutItem>();
		items.add(senderItem);
		
		payout.setSenderBatchHeader(senderBatchHeader).setItems(items);

		PayoutBatch batch = null;
		try {

			// ### Api Context
			// Pass in a `ApiContext` object to authenticate
			// the call and to send a unique request id
			// (that ensures idempotency). The SDK generates
			// a request id if you do not pass one explicitly.
			// ###Create Payout Synchronous
			batch = payout.create(apiContext, new HashMap<String, String>());
			System.out.println("Payout Batch With ID: " + batch.getBatchHeader().getPayoutBatchId());		
		} catch (PayPalRESTException e) {
			System.out.println("Created Single Synchronous Payout" +
			Payout.getLastRequest() +"With Error: " + e.getMessage());
		}
		return batch;

	}
	
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecute);
	}
}
