<?php

namespace App\Controller;

use App\Entity\Equipements;
use App\Form\EquipementsType;
use App\Repository\EquipementsRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\String\Slugger\SluggerInterface;
use MercurySeries\FlashyBundle\FlashyNotifier;
use Symfony\Component\Security\Core\Security;





class EquipementsController extends AbstractController
{

    #[Route('/equipements', name: 'app_equipements')]
    public function index(): Response
    {
        return $this->render('back.html.twig',[
            'controller_name' => 'EquipementsController',
            ]);
    }

    #[Route('/addequipements', name: 'add_equipements')]
    public function addEquipements(Request $request, ManagerRegistry $doctrine , SluggerInterface $slugger, FlashyNotifier $flashy,Security $security)
    {
        $equipement= new Equipements();
        $form= $this->createForm(EquipementsType::class,$equipement);
        $form->handleRequest($request) ;
        if($form->isSubmitted()&& $form->isValid()){
            $photo = $form->get('photo')->getData();
            if ($photo) {
                $originalFilename = pathinfo($photo->getClientOriginalName(), PATHINFO_FILENAME);
                $safeFilename = $slugger->slug($originalFilename);
                $newFilename = $safeFilename.'-'.uniqid().'.'.$photo->guessExtension();
                try {
                    $photo->move(
                        $this->getParameter('event_directory'),
                        $newFilename
                    );
                } catch (FileException $e) {

                }
                $equipement->setImage($newFilename);
            }

            $em=$doctrine->getManager();
            $user = $security->getUser();
            $equipement->setUser($user);
            $em->persist($equipement);
            $em->flush();
            $flashy->success('Equipement created!');


            return  $this->redirectToRoute("listequipements");
        }
        return $this->render("equipements/add.html.twig",['form'=> $form->createView()]);

    }

    #[Route('/listEquipements', name: 'listequipements')]
    public function listEquipements(EquipementsRepository $repository)
    {
        $equipement= $repository->findAll();
        return $this->render("equipements/list.html.twig",array("tabequipement"=>$equipement));
    }

    #[Route('/removeEquipements/{id}', name: 'remove_Equipements')]
    public function remove($id,EquipementsRepository $repository)
    {
        $equipement= $repository->removeById($id);
        return $this->redirectToRoute("listequipements");
    }

    #[Route('/updateEquipements/{id}', name: 'update_Equipements')]
    public function updateEquipementsForm($id, EquipementsRepository $repository, Request $request, ManagerRegistry $doctrine)
    {
        $equipement = $repository->find($id);
        $form = $this->createForm(EquipementsType::class, $equipement);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $doctrine->getManager();
            $entityManager->flush();

            return $this->redirectToRoute("listequipements");
        }

        return $this->render("equipements/update.html.twig", ['form' => $form->createView()]);
    }




}
