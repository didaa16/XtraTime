<?php

namespace App\Controller;

use App\Entity\Event;
use App\Form\EventType;
use App\Repository\EventRepository;
use App\Repository\SponsoRepository; 
use App\Repository\UserRepository;
use App\Repository\TerrainRepository;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\File\UploadedFile; // Import UploadedFile
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\HttpFoundation\JsonResponse;
use App\Entity\Participation;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

use Dompdf\Dompdf;




class EventController extends AbstractController
{
    #[Route('/event', name: 'app_event')]
    public function index(): Response
    {
        return $this->render('event/index.html.twig', [
            'controller_name' => 'EventController',
        ]);
    }


    //afficher 
    #[Route('/Affiche_event', name: 'event_Affiche')]
    public function Affiche(EventRepository $repository): Response
    {
        $events = $repository->findAll(); // Fetch all events
        return $this->render('event/Afficheevent.html.twig', ['e' => $events]);
    }


     //ajouter 
     #[Route('/Ajouter_event', name: 'app_Add')]
     public function Add(Request $request, SponsoRepository $sponsoRepository, TerrainRepository $terrainRepository, UserRepository $userRepository): Response
     {
         $event = new Event();
     
         // Fetch sponsor choices from the repository
         $sponsoChoices = $sponsoRepository->findAll();
         
         // Fetch terrain choices from the repository
         $terrainChoices = $terrainRepository->findAll();
         
         // Fetch user choices from the repository
         $userChoices = $userRepository->findAll();
         
         // Create the form with the sponsor, terrain, and user choices options
         $form = $this->createForm(EventType::class, $event, [
             'sponso_choices' => $sponsoChoices,
             'terrain_choices' => $terrainChoices,
             'user_choices' => $userChoices,
         ]);
         
         // Handle form submission
         $form->handleRequest($request);
         
         if ($form->isSubmitted() && $form->isValid()) {
             // Handle image upload
             $imageFile = $form->get('image')->getData();
     if ($imageFile) {
         // Get the original file name
         $fileName = $imageFile->getClientOriginalName();
                 
         // Move the file to the directory where images are stored
         $imageFile->move(
             $this->getParameter('image_directory'),
             $fileName
         );
                 
         // Update the 'image' property of the pack entity with the file name
         $event->setImage($fileName);
     }
             
            
             $entityManager = $this->getDoctrine()->getManager();
             $entityManager->persist($event);
             $entityManager->flush();
             
             return $this->redirectToRoute('event_Affiche');
         }
         
         return $this->render('event/Addevent.html.twig', [
             'f' => $form->createView(),
         ]);
       
     }



     //supprimer
#[Route('/event_delete/{idevent}', name: 'event_delete')]
public function event_delete($idevent, EventRepository $repository)
{
    $event = $repository->find($idevent);

    

        if (!$event) {
            throw $this->createNotFoundException('Auteur non trouvé');
        }

    $em = $this->getDoctrine()->getManager();
    $em->remove($event);
    $em->flush();

    return $this->redirectToRoute('event_Affiche');
}


// Modifier
#[Route('/event_edit/{idevent}', name: 'event_edit')]
// Modify the edit action in your controller
public function edit(EventRepository $repository, $idevent, Request $request): Response
{
    $event = $repository->find($idevent);

    // Check if the event exists
    if (!$event) {
        throw new NotFoundHttpException('Event not found');
    }
    
    // Get the existing image file name if event is found
    $previousImage = $event->getImage();
    
    // Create the edit form with the retrieved event and the previous image
    $form = $this->createForm(EventType::class, $event);
    
    // Handle form submission
    $form->handleRequest($request);
    
    if ($form->isSubmitted() && $form->isValid()) {
        // Handle image upload manually
        $imageFile = $form->get('image')->getData();
    
        if ($imageFile instanceof UploadedFile) {
            // Generate a unique name for the file before saving it
          
            $fileName = $imageFile->getClientOriginalName();

    
            // Move the file to the directory where images are stored
            $imageFile->move(
                $this->getParameter('image_directory'),
                $fileName
            );
    
            // Update the 'image' property of the event entity with the new file name
            $event->setImage($fileName);
    
            // Remove the previous image file if it exists
            if ($previousImage) {
                unlink($this->getParameter('image_directory') . '/' . $previousImage);
            }
        } else {
            // If no new image file has been uploaded, keep the previous image value
            $event->setImage($previousImage);
        }
    
        // Save the changes to the database
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->flush();
    
        return $this->redirectToRoute("event_Affiche");
    }
    
    return $this->render('event/editevent.html.twig', [
        'f' => $form->createView(),
        'event' => $event,
        'previousImage' => $previousImage, 
    ]);
}



#[Route('/Affiche_front', name: 'event_Affiche_front')]
public function Affiche_front(EventRepository $repository): Response
{
    $events = $repository->findAll(); // Fetch all events
    return $this->render('event/AfficheeventFront.html.twig', ['e' => $events]); // Pass 'events' as variable
}


#[Route('/Back_details/{idevent}', name: 'event_back_details')]

public function Bdetails($idevent, EventRepository $eventRepository): Response
{
    // Récupérer les détails de l'événement en fonction de l'ID
    $event = $eventRepository->find($idevent);

    // Vérifier si l'événement existe
    if (!$event) {
        throw $this->createNotFoundException('Event not found');
    }

    // Rendre la vue des détails de l'événement avec les données de l'événement
    return $this->render('event/Backdetails.html.twig', [
        'event' => $event,
    ]);
}



#[Route('/Front_details/{idevent}', name: 'event_front_details')]

public function details($idevent, EventRepository $eventRepository): Response
{
    // Récupérer les détails de l'événement en fonction de l'ID
    $event = $eventRepository->find($idevent);

    // Vérifier si l'événement existe
    if (!$event) {
        throw $this->createNotFoundException('Event not found');
    }

    // Rendre la vue des détails de l'événement avec les données de l'événement
    return $this->render('event/details.html.twig', [
        'event' => $event,
    ]);
}

#[Route('/participate/{eventId}', name: 'app_participate')]
public function participate(Request $request, EventRepository $eventRepository, UserRepository $utilisateursRepository, $eventId): Response
{
    // Récupérer l'événement à partir de son ID
    $event = $eventRepository->find($eventId);

    // Vérifier si l'événement existe
    if (!$event) {
        throw $this->createNotFoundException('L\'événement n\'existe pas');
    }

    $user = $utilisateursRepository->findOneBy(['pseudo' => 'habib']);

    // Créer une nouvelle instance de Participation
    $participation = new Participation();

    // Set the user and event
    $participation->setIduser($user);
    $participation->setIdevent($event);

    // Enregistrer la participation dans la base de données
    $entityManager = $this->getDoctrine()->getManager();
    $entityManager->persist($participation);
    $entityManager->flush();

    // Rediriger l'utilisateur vers la page de l'événement
    return $this->redirectToRoute('event_Affiche_front', ['id' => $eventId]);
}








#[Route('/stats', name: 'app_stats')]
public function stat(EventRepository $eventRepository): Response
{
    // Récupérer les événements avec leurs dates de début
    $events = $eventRepository->findAll();

    // Initialiser un tableau pour stocker le nombre d'événements par mois
    $eventsByMonth = [];

    // Parcourir tous les événements
    foreach ($events as $event) {
        // Récupérer le mois de la date de début de l'événement sous forme de nom complet
        $month = $event->getDatedebut()->format('F');
    
        // Incrémenter le compteur pour ce mois
        if (!isset($eventsByMonth[$month])) {
            $eventsByMonth[$month] = 1;
        } else {
            $eventsByMonth[$month]++;
        }
    }
    
    return $this->render('event/eventsByMonth.html.twig', [
        'eventsByMonth' => $eventsByMonth,
    ]);
}



//PDF



#[Route('/events/pdf', name: 'event_pdf')]
public function generatePdf(EventRepository $eventRepository): Response
{
    // Récupérer la liste des événements depuis le repository
    $events = $eventRepository->findAll();

    // Créer une instance de Dompdf
    $dompdf = new Dompdf();

    // Générer le HTML pour la liste des événements
    $html = $this->renderView('event/pdf.html.twig', ['events' => $events]);

    // Charger le HTML dans Dompdf
    $dompdf->loadHtml($html);

    // Rendre le PDF
    $dompdf->render();

    // Générer le nom du fichier PDF
    $pdfFileName = 'liste_des_evenements.pdf';

    // Envoyer le PDF en réponse avec l'en-tête Content-Disposition réglé sur "attachment"
    return new Response(
        $dompdf->output(),
        Response::HTTP_OK,
        [
            'Content-Type' => 'application/pdf',
            'Content-Disposition' => 'attachment; filename="' . $pdfFileName . '"',
        ]
    );
}

}
