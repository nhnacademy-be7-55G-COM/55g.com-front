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
