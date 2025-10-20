document.addEventListener('DOMContentLoaded',()=>{
  const modal=document.getElementById('assignModal');
  const open=document.getElementById('openAssign');
  const close=document.getElementById('closeAssign');
  const cancel=document.getElementById('cancelAssign');
  if(open && modal){open.addEventListener('click',()=>modal.classList.add('show'));}
  if(close && modal){close.addEventListener('click',()=>modal.classList.remove('show'));}
  if(cancel && modal){cancel.addEventListener('click',()=>modal.classList.remove('show'));}
});
