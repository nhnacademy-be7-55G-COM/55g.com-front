const callPointHistoryPage = async (tableSelector, { size, page }) => {
  let totalPage = -1;
  let totalElements = -1;
  console.log(`print request: size: ${size}, page: ${page}`);
  // const response = await axios.get('/point/support/history', { size: size, page: page});
  const response = await axios.get(`/point/support/history?size=${size}&page=${page}`);

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

    const remainingPoint= document.createElement('td');
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

const callOrderHistoryPage = async (tableSelector, {startDate, endDate}) => {
  const table = document.querySelector(tableSelector);
  const orders = (await axios.get('/mypage/support/orders',
      {params: { startDate: startDate, endDate: endDate}}
      )).data;

  const tbody = table.querySelector('tbody');
  tbody.innerHTML = '';
  orders.forEach(order => {
    const tr = document.createElement('tr');
    const orderId = document.createElement('td');
    orderId.innerHTML = order.orderId;

    const totalPrice= document.createElement('td');
    totalPrice.innerHTML = order.totalPrice;

    const representTitle= document.createElement('td');
    representTitle.innerHTML = order.representTitle;

    const orderedAt= document.createElement('td');
    orderedAt.innerHTML = order.orderedAt;

    const quantity = order.totalQuantity;
    const kind = order.totalKind;

    if (kind > 1) {
      representTitle.innerHTML = `<p>${representTitle.innerHTML} 등</p><p>${kind} 종류 ${quantity} 권</p>`;
    } else if (quantity > 1){
      representTitle.innerHTML = `${representTitle.innerHTML} ${quantity}권`;
    }
    tr.append(orderId, representTitle, totalPrice, orderedAt);
    tbody.appendChild(tr);
  });
}