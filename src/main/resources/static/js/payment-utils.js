const execPostCode = ({ zipSelector, addressSelector, addressDetailSelector, addressExtraSelector }) => {
  new daum.Postcode({
    oncomplete: data => {
      let addr = '';
      let extraAddr = '';

      if (data.userSelectedType === 'R') {
        addr = data.roadAddress;
      } else {
        addr = data.jibunAddress;
      }
      // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
      if(data.userSelectedType === 'R'){
        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
        if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
          extraAddr += data.bname;
        }
        // 건물명이 있고, 공동주택일 경우 추가한다.
        if(data.buildingName !== '' && data.apartment === 'Y'){
          extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
        }
        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
        if(extraAddr !== ''){
          extraAddr = ' (' + extraAddr + ')';
        }
        // 조합된 참고항목을 해당 필드에 넣는다.
        document.querySelector(addressExtraSelector).value = extraAddr;

      } else {
        document.querySelector(addressExtraSelector).value = '';
      }

      // 우편번호와 주소 정보를 해당 필드에 넣는다.
      document.querySelector(zipSelector).value = data.zonecode;
      document.querySelector(addressSelector).value = addr;
      // 커서를 상세주소 필드로 이동한다.
      document.querySelector(addressDetailSelector).focus();
    }
  }).open();
};

/*
  customer = { key, name, email }
 */

const cardPaymentDefault = {
  useEscrow: false,
  flowMode: 'DEFAULT',
  useCardPoint: false,
  useAppCardOnly: false
};

const tossPaymentReady = async ({
    orderId,
    orderName,
    method,
    amount,
    successUrl,
    failUrl,
    customer
}, clientKey) => {
  const tossPayments = TossPayments(clientKey);
  const customerKey = customer.key;
  const payment = tossPayments.payment({ customerKey });

  const paymentInfo = {
    method: method,
    amount: {
      currency: amount.currency,
      value: amount.value
    },
    orderId: orderId,
    orderName: orderName,
    successUrl: successUrl,
    failUrl: failUrl,
    customerEmail: customer.email,
    customerName: customer.name
  };

  if (method === 'CARD') {
    paymentInfo.card = structuredClone(cardPaymentDefault);
  }
  await payment.requestPayment(paymentInfo);
}

const returnToIndex = () => {
  window.location.href='/';
}

const fetchShoppingCart = async () => {
  return (await axios.get('/payment/support/fetch-cart')).data;
}
//
// const createNewOrder = (deliveryInfo) => {
//   return axios.post('/payment/support/create-order', deliveryInfo);
// }

const createNewOrder = ({
  customerId, delivery, elements, netPrice, totalPrice
}) => {
  return axios.post('/payment/support/create-order', {
    customerId, delivery, elements, netPrice, totalPrice
  });
};

const isNotBlank = (...args) => {
  for (let i=0; i<args.length; i++) {
    if (args[i] === undefined || args[i] === '') {
      return false;
    }
  }
  return true;
}

const getDeliveryInformation = ({
    zipSelector,
    addressSelector,
    addressDetailSelector,
    addressExtraSelector
}, feeId, receiveDateSelector, receiverNameSelector) => {
  const zip = document.querySelector(zipSelector).value;
  const address= document.querySelector(addressSelector).value;
  const addressDetail= document.querySelector(addressDetailSelector).value;
  const addressExtra= document.querySelector(addressExtraSelector).value;
  const receiveDate = document.querySelector(receiveDateSelector).value;
  const receiverName = document.querySelector(receiverNameSelector).value;

  // extra는 비어도 허용
  if (!isNotBlank(zip, address, addressDetail, receiveDate)) {
    throw "주소가 비어있음";
  }
  const addressString = `${zip}, ${address} ${addressDetail} ${addressExtra}`.trim();
  return {
    address: addressString,
    deliveryFeeId: feeId,
    receivedDate: receiveDate,
    receiverName: receiverName
  };
};


const getDeliveryInfoFromSaved = (selectedAddress, feeId, receiveDateSelector, receiverNameSelector) => {
  const receiveDate = document.querySelector(receiveDateSelector).value;
  const receiverName = document.querySelector(receiverNameSelector).value;

  if (selectedAddress === null || selectedAddress === '') {
    throw "주소가 비어있음";
  }
  return {
    address: selectedAddress,
    deliveryFeeId: feeId,
    receivedDate: receiveDate,
    receiverName: receiverName
  };
};
// 적립률을 여기서 계산하지 말고 컨트롤러에서 계산하도록 하자.
// 아니아니 적립률을 보여줘야 할거 아니냐..
// 근데 그러면 여기서 계산하는게 아니라 미리 계산해서 보여줘야 하는게 아닌지?
const convertCartToRequest = ({
    id, paperSelector, quantity, totalPrice
}) => {
  const paper = document.querySelector(paperSelector);
  const val = parseInt(paper.value);
  const paperId = val < 0 ? null : val;

  return {
    bookId: id,
    wrappingPaperId: paperId,
    quantity: quantity,
    totalPrice: totalPrice
  };
};

const onPaymentFail = (orderDataId) => {
  return axios.delete(`/payment/support/order`);
}

/*
 * origin: 말 그대로 원가격 [sum(book_price*quantity)]
 * discount: 할인가격 [sum(book_price*discount_rate - coupon)]
 * beforeDelivery: 최종 가격에서 배달비를 뺀 가격 [totalPrice-delivery]  -> 이 가격을 기준으로 배달비 책정
 * totalPrice: 최종 결제 가격
 * pointUse: 포인트 사용량
 */
const getCopyOfPriceInfo = () => {
  return { originPrice: 0, discountPrice: 0, beforeDelivery: 0, totalPrice: new Decimal(0), pointUse: 0 };
}

const calcFromCart = (priceInfo, cart) => {
  let origin = 0;
  let discount = 0;
  cart.forEach(book => {
    origin += book.price * book.quantity;
    discount += book.discountPrice * book.quantity;
  });
  priceInfo.originPrice = origin;
  priceInfo.discountPrice = discount;
  priceInfo.beforeDelivery = origin-discount;
  priceInfo.totalPrice = new Decimal(priceInfo.beforeDelivery);
}