const hideEventListener = () => {
    const elements = document.querySelectorAll('.sidenav');
    const options = {
        edge : 'left',
        inDuration : 300,
        outDuration : 300,
        preventScrolling : true
    }
    const instances = M.Sidenav.init(elements, options)
}

document.addEventListener('DOMContentLoaded', hideEventListener);
