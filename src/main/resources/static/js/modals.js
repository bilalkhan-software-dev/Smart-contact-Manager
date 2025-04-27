console.log("contact modals loaded");

const viewContactModals = document.getElementById('view_contact_modals');
const baseUrl = window.location.origin ;


// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
    id: 'view_contact_modals',
    override: true
};

const contactModal = new Modal(viewContactModals, options, instanceOptions);

function openContact() {
    contactModal.show();
}

function closeContact() {
    contactModal.hide();

}

async function loadContactData(id) {
    console.log(id);
    try {
        // const data = await (await fetch('http://localhost:8081/api/contacts/${id}')
        const data = await (await fetch(`${baseUrl}/api/contacts/${id}`)).json();

    // ).json();
        console.log(data);
        document.getElementById("contact_name").innerHTML = data.name ;
        document.getElementById("contact_email").innerHTML = data.email ;
        document.getElementById("contact_about").innerHTML = data.description ;
        document.getElementById("contact_address").innerHTML = data.address ;
        document.getElementById("contact_phone").innerHTML = data.phoneNum ;
        document.getElementById("contact_image").src = data.profilePic ;

        const  contactFavourite = document.getElementById("contact_favorite") ;

        if (data.favourite){
                contactFavourite.innerHTML =
                    "<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
        }else {
            contactFavourite.innerHTML = "Not Favourite Contact";
        }
        document.getElementById("contact_website").href = data.websiteLink ;
        document.getElementById("contact_website").innerHTML = data.websiteLink ;
        document.getElementById("contact_linkedIn").href = data.linkedListLink ;
        document.getElementById("contact_linkedIn").innerHTML = data.linkedListLink ;
        openContact() ;
    } catch (error) {
        console.log("Error occurs : ", error);
    }
}
async function deleteContact(id) {
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#1278ec",
        cancelButtonColor: "#ef0a0a",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            // Swal.fire({
            //     // title: "Deleted!",
            //     // text: "Your contact has been deleted.",
            //     // icon: "success"
            // });

            const  url  = `${baseUrl}/user/contacts/delete/${id}` ;
            window.location.replace(url);
            console.log("Contact deleted");
        }
    });
}




