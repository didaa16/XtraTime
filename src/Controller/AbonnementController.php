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
    public function showAbonnementForm(Request $request, $idp)
    {
        // Récupérer le pack choisi par l'utilisateur
        $pack = $this->getDoctrine()->getRepository(Pack::class)->find($idp);

        // Créer une nouvelle instance d'Abonnement
        $abonnement = new Abonnement();

        // Pré-remplir le champ nomPack avec le nom du pack choisi
        $abonnement->setNompack($pack->getNom());

        // Créer le formulaire en passant le pack choisi
        $form = $this->createForm(AbonnementType::class, $abonnement, [
            'packs' => [$pack], // Passer le pack choisi au formulaire
        ]);

        // Gérer la soumission du formulaire
        $form->handleRequest($request);

        // Si le formulaire est soumis et valide, enregistrer l'abonnement
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($abonnement);
            $entityManager->flush();

            // Rediriger l'utilisateur vers une autre page ou afficher un message de succès
            return $this->redirectToRoute('nom_de_la_route_vers_la_page_de_confirmation');
        }

        // Afficher le formulaire
        return $this->render('abonnement/AjouterAbonnement.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}/edit', name: 'app_abonnement_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Abonnement $abonnement, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(AbonnementType::class, $abonnement);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_abonnement_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('abonnement/edit.html.twig', [
            'abonnement' => $abonnement,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_abonnement_delete', methods: ['POST'])]
    public function delete(Request $request, Abonnement $abonnement, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$abonnement->getId(), $request->request->get('_token'))) {
            $entityManager->remove($abonnement);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_abonnement_index', [], Response::HTTP_SEE_OTHER);
    }
}
