const url = "https://marginsite.ovh:8443/api";

function getAuthUrl() {
  return `${url}/auth`;
}
function getCostUrl() {
  return `${url}/cost`;
}
function getCosFixedUrl() {
  return `${url}/cost/fixed`;
}
function getCostVariableUrl() {
  return `${url}/cost/variable`;
}
function getRecipeUrl() {
  return `${url}/recipe`;
}
function getStatUrl() {
  return `${url}/stat`;
}
function getLocationUrl() {
  return `${url}/location`;
}
