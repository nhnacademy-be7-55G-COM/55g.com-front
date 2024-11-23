const deliveryMap = {
  PREPARING: '배송준비중',
  SHIPPING: '배송중',
  DELIVERED: '배송 완료'
};

const detailMap = {
  COMPLETE: '주문 완료',
  CONFIRM: '주문 확정',
  RETURN: '반품',
  CANCEL: '주문 취소'
};

const callPointHistoryPage = async (tableSelector, {size, page}) => {
  let totalPage = -1;
  let totalElements = -1;
  console.log(`print request: size: ${size}, page: ${page}`);
  // const response = await axios.get('/point/support/history', { size: size, page: page});
  const response = await axios.get(
      `/point/support/history?size=${size}&page=${page}`);

  const table = document.querySelector(tableSelector);
  const tbody = table.querySelector('tbody');
  tbody.innerHTML = '';
  // 일반적으로 JSON 파싱이 되어있음.
  const histories = response.data.content;
  histories.forEach(history => {
    const row = document.createElement('tr');
    const id = document.createElement('td');
    id.innerHTML = history['id'];

    const pointSource = document.createElement('td');
    pointSource.innerHTML = history['pointSource'];

    const point = document.createElement('td');
    point.innerHTML = history['point'];

    const remainingPoint = document.createElement('td');
    remainingPoint.innerHTML = history['remainingPoint'];

    const createdAt = document.createElement('td');
    createdAt.innerHTML = history['createdAt'];

    row.append(id, pointSource, point, remainingPoint, createdAt);
    tbody.appendChild(row);
  });
  totalPage = response.data.totalPage;
  totalElements = response.data.totalElements;

  console.log(`totalPage: ${totalPage}, totalElements: ${totalElements}`);
  return {totalPage, totalElements};
}

const cancelDetail = (id) => {
  axios.put(`/mypage/support/orders/cancel/${id}`, { cancelReason: "이런저런사유" })
    .then(response => {
      alert("주문이 취소되었습니다. 환불에 시간이 걸릴 수 있습니다...");
      window.location.reload();
    })
    .catch(err => {
      console.log(err);
      alert("주문 취소에 실패했습니다. 다시 시도해 주세요.");
    });
}
const imageInputTemplate = `<div><input class="form-control mb-1 mt-1" name="refundImage" type="file" accept="image/*" /></div>`;
const extendImageInput = (rowSelector) => {

  const imageContainer = document.querySelector(
      `${rowSelector} .image-container`);
  if (imageContainer.childElementCount >= 5) {
    alert("이미지 추가는 4개까지 가능합니다!");
    return;
  }
  imageContainer.innerHTML += imageInputTemplate;
}
const extendRefundDiv = (btn, id) => {
  document.querySelector(`#row-refund-${id}`).style.display = 'table-row';
}
const imageFileLimit = 2*1024*1024;

const refundDetail = (id) => {
  const refundRow = document.querySelector(`#row-refund-${id}`);
  const imageInputs = refundRow.querySelectorAll(`input[name="refundImage"]`);
  const refundContent = refundRow.querySelector('.refund-reason-area');
  const refundTypeSelector = refundRow.querySelector('select');

  const multipart = new FormData();
  // 파일 하나당 크기 제한 2MB
  const headers = { 'Content-Type': 'multipart/form-data' };
  imageInputs.forEach(input => {
    if (input.files[0].size >= imageFileLimit) {
      console.log(input.files[0]);
      alert('이미지 파일은 2MB를 넘을 수 없습니다.');
      return;
    }
    multipart.append('refundImages', input.files[0])
  });
  multipart.append('orderDetailId', id);
  multipart.append('content', refundContent.value);
  multipart.append('type', refundTypeSelector.value);

  return axios.post(`/mypage/support/orders/refund`, multipart, headers)
    .then(response => {
      alert("반품이 신청되었습니다. 반품한 책에 결제한 만큼 포인트로 환불됩니다.");
    })
    .catch(err => {
      console.log(err);
      alert("반품에 실패했습니다. 다시 시도해 주세요.");
    });
}
const applyOrderDetailTemplate = (tr) => {
  const myModal = detailModalInstance;
  tr.addEventListener('click', async () => {
    myModal.show();
    const orderId = tr.getAttribute('data-order-id');
    const info = (await axios.get('/mypage/support/orders/info', {params: {orderId}})).data;
    const delivery = info['delivery'];
    const tbody = detailModalBody.querySelector('#detail-table-books > tbody');
    // 주문상세 삽입
    info.details.forEach(detail => {
      let dangerButton = '';
      if (detail.orderDetailType === 'COMPLETE') {
        if (delivery.status === 'PREPARING')
          dangerButton = `<button onclick="cancelDetail(${detail.orderDetailId})" class="btn btn-outline-danger button-cancel">주문취소</button>`;
        else
          dangerButton = `<button onclick="extendRefundDiv(this, ${detail.orderDetailId})" class="btn btn-outline-danger button-cancel">반품하기</button>`;
      }
      tbody.innerHTML += `
        <tr data-order-id="${detail.orderDetailId}">
        <td>${detail.bookTitle}</td>
        <td>${detail.quantity}</td>
        <td>${detail.totalPrice}</td>
        <td>${detail.accumulationPrice}</td>
        <td>${detail.wrappingPaperName == null ? '' : detail.wrappingPaperName}</td>
        <td>${dangerButton}</td>
        </tr>
      `;
      // TODO: 환불타입 하드코딩 수정
      let titleSummary = detail.bookTitle;
      if (titleSummary.length >= 26)
        titleSummary = titleSummary.substring(0, 26) + '...';

      tbody.innerHTML += `
        <tr id="row-refund-${detail.orderDetailId}" style="display: none;">
          <td colspan="6">
            <div>
              <div class="d-flex flex-row mb-1">
                <p class="flex-grow-1 me-auto" style="margin-bottom: 0; font-size=18px">환불: ${titleSummary}</p>
                <select class="ms-3" name="refundType" class="form-select">
                  <option value="1" selected>단순변심</option>
                  <option value="2">오배송</option>
                  <option value="3">배송 지연</option>
                  <option value="4">파손/파본</option>
                  <option value="5">잘못 주문함</option>
                  <option value="6">기타</option>
                </select>
              </div>
              <textarea class="form-control-sm refund-reason-area w-100 mb-1" rows="3" placeholder="환불 사유 입력..." required></textarea>
              <div class="image-container">
                <div>
                  <button type="button" class="refund-image-button btn btn-warning text-white" onclick="extendImageInput('#row-refund-${detail.orderDetailId}')" style="padding: 10px">이미지 추가</button>
                  <button 
                    type="button" class="btn btn-danger refund-image-button ms-4"
                    onclick="refundDetail(${detail.orderDetailId})"
                  >반품신청</button>
                </div>
              </div>
            </div>
          </td>
        </tr>
      `;
    });

    // 배송정보 삽입
    orderDetailModal.querySelector('#detail-receiverName').textContent = info.delivery.receiverName;
    orderDetailModal.querySelector('#detail-address').textContent = info.delivery.address;
    orderDetailModal.querySelector('#detail-invoiceNumber').textContent = info.delivery.invoiceNumber;
    orderDetailModal.querySelector('#detail-receivedDate').textContent = info.delivery.receivedDate;
    orderDetailModal.querySelector('#detail-shippingDate').textContent = info.delivery.shippingDate;
    orderDetailModal.querySelector('#detail-status').textContent = info.delivery.status;

    // display 활성화.
    detailModalBody.querySelector('.spinner-border').style.display = 'none';
    detailModalBody.querySelector('#detail-body').style.display = 'block';
  });
};

const callOrderHistoryPage = async (tableSelector, {startDate, endDate}) => {
  const table = document.querySelector(tableSelector);
  const orders = (await axios.get('/mypage/support/orders',
      {params: {startDate: startDate, endDate: endDate}}
  )).data;

  const tbody = table.querySelector('tbody');
  tbody.innerHTML = '';
  orders.forEach(order => {
    const tr = document.createElement('tr');
    tr.setAttribute('data-order-id', order.orderId);

    const orderId = document.createElement('td');
    orderId.innerHTML = order.orderId;

    const totalPrice = document.createElement('td');
    totalPrice.innerHTML = order.totalPrice;

    const representTitle = document.createElement('td');
    representTitle.innerHTML = order.representTitle;

    const orderedAt = document.createElement('td');
    orderedAt.innerHTML = order.orderedAt;

    const quantity = order.totalQuantity;
    const kind = order.totalKind;

    if (kind > 1) {
      representTitle.innerHTML = `<p>${representTitle.innerHTML} 등</p><p>${kind} 종류 ${quantity} 권</p>`;
    } else if (quantity > 1) {
      representTitle.innerHTML = `${representTitle.innerHTML} ${quantity}권`;
    }

    tr.append(orderId, representTitle, totalPrice, orderedAt);
    applyOrderDetailTemplate(tr);
    tbody.appendChild(tr);
  });
}

// TODO: 환불정보는 나중에...
const orderDetailModalTemplate = `
  <div class="spinner-border text-primary"></div>
  <div style="display: none;" id="detail-body">
    <div>
      <h4>주문정보</h4>
      <table class="table" id="detail-table-books">
        <thead>
        <tr>
          <th scope="col" style="width: 60%;">제목</th>
          <th scope="col">권 수</th>
          <th scope="col">가격</th>
          <th scope="col">적립</th>
          <th scope="col">포장지</th>
          <th scope="col">주문취소</th>
        </tr>
        </thead>
        <tbody></tbody>
      </table>
    </div>
    <div class="card-container">
      <div class="card">
        <div class="card-header text-center text-black">
          <h4>배송 정보</h4>
        </div>
        <div class="card-body">
          <p><strong>수신자:</strong><span id="detail-receiverName"></span></p>
          <p><strong>배송 주소:</strong><span id="detail-address"></span></p>
          <p><strong>송장 번호:</strong><span id="detail-invoiceNumber"></span></p>
          <p><strong>받는 날:</strong><span id="detail-receivedDate"></span></p>
          <p><strong>출고 날:</strong><span id="detail-shippingDate"></span></p>
          <p><strong>배송 상태:</strong><span id="detail-status"></span></p>
        </div>
      </div>
    </div>
  </div>
`;
// <h4>배송정보</h4>
// <div class="d-flex flex-row">
//   <div class="small-card">
//     <h5>수신자</h5>
//     <p id="receiverName"></p>
//   </div>
//   <div class="small-card">
//     <h5>송장번호</h5>
//     <p id="invoiceNumber"></p>
//   </div>
//   <div class="small-card">
//     <h5>배송상태</h5>
//     <p id="deliveryStatus"></p>
//   </div>
// </div>
// <div class="d-flex flex-row">
//   <div class="small-card">
//
//   </div>
// </div>
// </div>
