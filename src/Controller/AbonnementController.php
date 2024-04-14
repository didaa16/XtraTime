<?php

namespace App\Controller;

use App\Entity\Abonnement;
use App\Form\AbonnementType;
use Symfony\Component\Form\Extension\Core\Type\DateType;

use App\Repository\AbonnementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Entity\Pack;
use App\Entity\Terrain;
#[Route('/abonnement')]
class AbonnementController extends AbstractController
{
    #[Route('/', name: 'app_abonnement_index', methods: ['GET'])]
    public function index(AbonnementRepository $abonnementRepository): Response
    {
        return $this->render('abonnement/tables-listjsAbonnement.html.twig', [
            'abonnements' => $abonnementRepository->findAll(),
        ]);
    }

    #[Route('/{idp}', name: 'app_abonnement_form', methods: ['GET', 'POST'])]
    public function showAbonnementForm(Request $request, $idp): Response
{
    // Récupérer le pack choisi par l'utilisateur
    $pack = $this->getDoctrine()->getRepository(Pack::class)->find($idp);

    // Vérifier si le pack existe
    if (!$pack) {
        throw $this->createNotFoundException('Pack non trouvé pour l\'ID : '.$idp);
    }

    // Créer une nouvelle instance d'Abonnement
    $abonnement = new Abonnement();

    // Pré-remplir le champ nomPack avec le nom du pack choisi
    $abonnement->setNompack($pack->getNom());

    // Créer le formulaire en passant le pack choisi
    $form = $this->createForm(AbonnementType::class, $abonnement, [
        'pack' => $pack, // Passer le pack choisi au formulaire
    ]);
    

    // Gérer la soumission du formulaire
    $form->handleRequest($request);

    // Si le formulaire est soumis et valide, enregistrer l'abonnement
    if ($form->isSubmitted() && $form->isValid()) {

        function convertirDureeEnJours($duree)
            {
                $nombreMois = intval($duree);
                $joursParMois = 30; // ou tout autre nombre de jours par mois
                return $nombreMois * $joursParMois;
            }

            // Utilisation de la fonction pour convertir la durée du pack
            $dureePack = convertirDureeEnJours($pack->getDuree());
            $terrain = $abonnement->getTerrain();

            // Définir la valeur de 'nomterrain' avec le nom du terrain choisi
            $abonnement->setNomterrain($terrain->getNom());
            // Calculer le prix avant réduction
            $prixTerrain = $terrain->getPrix();
            $prix = $prixTerrain * $dureePack;
            $abonnement->setPrix($prix);

            // Calculer le prix après réduction
            $reduction = $pack->getReduction();
            $prixTotal = $prix * (1 - $reduction / 100);

            $abonnement->setPrixTotal($prixTotal);

        // Enregistrer l'abonnement dans la base de données
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->persist($abonnement);
        $entityManager->flush();

        // Rediriger l'utilisateur vers une autre page ou afficher un message de succès
        return $this->redirectToRoute('app_abonnement_details', ['id' => $abonnement->getId()]);
    }

    // Afficher le formulaire
    return $this->render('abonnement/AjouterAbonnement.html.twig', [
        'form' => $form->createView(),
    ]);
}

    
    
    

    #[Route('/{id}/edit', name: 'app_abonnement_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Abonnement $abonnement, EntityManagerInterface $entityManager): Response
{
    // Récupérer le pack associé à l'abonnement
    $pack = $abonnement->getPack();

    // Vérifier si le pack existe
    if (!$pack) {
        throw $this->createNotFoundException('Pack non trouvé pour cet abonnement');
    }

    // Créer le formulaire en passant le nom du pack
    $form = $this->createForm(AbonnementType::class, $abonnement, [
        'pack' => $pack,
    ]);

    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $entityManager->flush();

        // Rediriger l'utilisateur vers une autre page en passant l'ID de l'abonnement comme paramètre
        return $this->redirectToRoute('app_abonnement_details', ['id' => $abonnement->getId()]);
    }

    return $this->renderForm('abonnement/editerAbonnement.html.twig', [
        'abonnement' => $abonnement,
        'form' => $form,
    ]);
}


    

    #[Route('/{id}/delete', name: 'app_abonnement_deleteP', methods: ['GET', 'POST'])]
    public function deleteAbonnement($id, EntityManagerInterface $entityManager): Response
    {
        // Récupérer l'abonnement à supprimer
        $abonnement = $entityManager->getRepository(Abonnement::class)->find($id);
    
        // Vérifier si l'abonnement existe
        if (!$abonnement) {
            throw $this->createNotFoundException('Abonnement non trouvé pour l\'ID : '.$id);
        }
    
        // Supprimer l'abonnement
        $entityManager->remove($abonnement);
        $entityManager->flush();
        
        // Rediriger vers la page principale ou toute autre page appropriée après la suppression
        return $this->redirectToRoute('app_packC_show'); // Remplacez 'page_principale' par le nom de votre route pour la page principale du site
    }
    

    #[Route('/{id}/deleteF', name: 'app_abonnement_deleteF', methods: ['GET', 'POST'])]
    public function delete($id, EntityManagerInterface $entityManager): Response
    {
        // Récupérer l'abonnement à supprimer
        $abonnement = $entityManager->getRepository(Abonnement::class)->find($id);
    
        // Vérifier si l'abonnement existe
        if (!$abonnement) {
            throw $this->createNotFoundException('Abonnement non trouvé pour l\'ID : '.$id);
        }
    
        // Supprimer l'abonnement
        $entityManager->remove($abonnement);
        $entityManager->flush();
        
        // Rediriger vers la page principale ou toute autre page appropriée après la suppression
        return $this->redirectToRoute('app_front'); // Remplacez 'page_principale' par le nom de votre route pour la page principale du site
    }
    


   #[Route('/abonnement/{id}', name: 'app_abonnement_details')]
   
   public function showAbonnementDetails($id): Response
{
    // Récupérer les détails de l'abonnement en fonction de l'ID
    $abonnement = $this->getDoctrine()->getRepository(Abonnement::class)->find($id);

    // Vérifier si l'abonnement existe
    if (!$abonnement) {
        throw $this->createNotFoundException('Abonnement non trouvé pour l\'ID : '.$id);
    }

    // Vérifier si l'abonnement a le pack associé
    if ($abonnement->getPack()) {
        // Si oui, récupérez le nom du pack
        $nomPack = $abonnement->getPack()->getNom();
    } 
    // Charger les associations avec les entités Terrain et Pack
    $abonnement->getTerrain();
    $abonnement->getPack();

    // Récupérer les informations nécessaires sur l'abonnement
    $nomUser = $abonnement->getNomuser();
    $numTel = $abonnement->getNumtel();
    $dateDebut = $abonnement->getDate();
    $nomTerrain = $abonnement->getTerrain() ? $abonnement->getTerrain()->getNom() : null;
    $nomPack = $abonnement->getNompack();
    $prixAvantReduction = $abonnement->getPrix();
    $prixApresReduction = $abonnement->getPrixtotal();

    // Afficher les détails dans une vue Twig
    return $this->render('abonnement/detailsAbonnement.html.twig', [
        'abonnement' => $abonnement, // Transmettre l'objet abonnement au template Twig
        'nomUser' => $nomUser,
        'numTel' => $numTel,
        'dateDebut' => $dateDebut,
        'nomTerrain' => $nomTerrain,
        'nomPack' => $nomPack,
        'prixAvantReduction' => $prixAvantReduction,
        'prixApresReduction' => $prixApresReduction,
    ]);
}

   



}
    

