<?php

namespace App\Controller;

use App\Entity\Equipements;
use App\Entity\Reservations;
use App\Form\ReservationsType;
use App\Repository\ReservationsRepository;
use Doctrine\ORM\EntityManager;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\Security\Core\Security;

class ReservationsController extends AbstractController
{

    #[Route('/index', name: 'test')]
    public function test(): Response
    {
        return $this->render('front.html.twig' ,[
            'title' => 'welcome',
        ]);
    }



    #[Route('/reservations', name: 'app_reservations')]
    public function index(): Response
    {
        return $this->render('reservations/index.html.twig', [
            'controller_name' => 'ReservationsController',
        ]);
    }
    #[Route('/addReservations', name: 'add_Reservations')]
    public function addReservations(Request $request, ManagerRegistry $doctrine,Security $security)
    {
        //  $equipements = $doctrine->getRepository(Equipements::class)->findAll();
        $reservation = new Reservations();
        $form = $this->createForm(ReservationsType::class, $reservation);
        $form->handleRequest($request);
        if($form->isSubmitted()&&$form->isValid())
        {
            $entityManager=$doctrine->getManager();
            $user=$security->getUser();
            $reservation->setUser($user);
            $entityManager->persist($reservation);
            $entityManager->flush();

            return $this->redirectToRoute("listReservations");
        }

        return $this->render("reservations/add.html.twig", [
            'form'=> $form->createView(),
        ]);
    }

    #[Route('/listReservations',name: 'listReservations')]
    public function listReservations(ReservationsRepository $repository)
    {
        $reservations = $repository->findAll();
        return $this->render("reservations/list.html.twig",[
            'tablereservations'=>$reservations
        ]);
    }

    #[Route('/listtReservations',name: 'listReservation')]
    public function listtReservations(ReservationsRepository $repository)
    {
        $reservations = $repository->findAll();
        return $this->render("reservations/listt.html.twig",[
            'reservations'=>$reservations
        ]);
    }

    #[Route('/removeReservations/{id}',name: 'remove_Reservations')]
    public function remove($id,ReservationsRepository $repository)
    {
        $reservation = $repository->removeById($id);
        return $this->redirectToRoute("listReservations");
    }
    #[Route('updateReservations/{id}', name: 'update_Reservations')]
    public function updateReservationForm($id, ReservationsRepository $repository, Request $request,ManagerRegistry $doctrine)
    {
        $equipements = $doctrine->getRepository(Equipements::class)->findAll();
        $reservation = $repository->find($id);
        $form = $this->createForm(ReservationsType::class,$reservation);
        $form-> handleRequest($request);

        if($form->isSubmitted()&&$form->isValid())
        {
            $entityManager = $doctrine->getManager();
            $entityManager->flush();

            return $this->redirectToRoute("listReservations");
        }

        return  $this->render('reservations/update.html.twig',[
            'form'=>$form->createView(),
            'tablereservation' => $reservation,
            'equipements' => $equipements,
        ]);
    }
}
