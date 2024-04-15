<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240415120327 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE equipements (id INT AUTO_INCREMENT NOT NULL, stock INT DEFAULT NULL, nom VARCHAR(255) DEFAULT NULL, description VARCHAR(255) DEFAULT NULL, type VARCHAR(50) DEFAULT NULL, prix INT DEFAULT NULL, image VARCHAR(255) DEFAULT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE reservations (id INT AUTO_INCREMENT NOT NULL, user_id INT DEFAULT NULL, date VARCHAR(255) DEFAULT NULL, duree VARCHAR(255) DEFAULT NULL, prix INT DEFAULT NULL, INDEX IDX_4DA239A76ED395 (user_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE reservations_terrain (reservations_id INT NOT NULL, terrain_id INT NOT NULL, INDEX IDX_454E216FD9A7F869 (reservations_id), INDEX IDX_454E216F8A2D8B41 (terrain_id), PRIMARY KEY(reservations_id, terrain_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE reservations_equipements (reservations_id INT NOT NULL, equipements_id INT NOT NULL, INDEX IDX_2A6AE999D9A7F869 (reservations_id), INDEX IDX_2A6AE999852CCFF5 (equipements_id), PRIMARY KEY(reservations_id, equipements_id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE terrain (id INT AUTO_INCREMENT NOT NULL, user_id INT DEFAULT NULL, ref INT NOT NULL, nom VARCHAR(255) NOT NULL, capacite INT NOT NULL, type VARCHAR(255) NOT NULL, prix INT NOT NULL, disponibilite VARCHAR(255) NOT NULL, img VARCHAR(255) NOT NULL, INDEX IDX_C87653B1A76ED395 (user_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE user (id INT AUTO_INCREMENT NOT NULL, username VARCHAR(180) NOT NULL, roles JSON NOT NULL COMMENT \'(DC2Type:json)\', password VARCHAR(255) NOT NULL, UNIQUE INDEX UNIQ_IDENTIFIER_USERNAME (username), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE reservations ADD CONSTRAINT FK_4DA239A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
        $this->addSql('ALTER TABLE reservations_terrain ADD CONSTRAINT FK_454E216FD9A7F869 FOREIGN KEY (reservations_id) REFERENCES reservations (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE reservations_terrain ADD CONSTRAINT FK_454E216F8A2D8B41 FOREIGN KEY (terrain_id) REFERENCES terrain (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE reservations_equipements ADD CONSTRAINT FK_2A6AE999D9A7F869 FOREIGN KEY (reservations_id) REFERENCES reservations (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE reservations_equipements ADD CONSTRAINT FK_2A6AE999852CCFF5 FOREIGN KEY (equipements_id) REFERENCES equipements (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE terrain ADD CONSTRAINT FK_C87653B1A76ED395 FOREIGN KEY (user_id) REFERENCES user (id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE reservations DROP FOREIGN KEY FK_4DA239A76ED395');
        $this->addSql('ALTER TABLE reservations_terrain DROP FOREIGN KEY FK_454E216FD9A7F869');
        $this->addSql('ALTER TABLE reservations_terrain DROP FOREIGN KEY FK_454E216F8A2D8B41');
        $this->addSql('ALTER TABLE reservations_equipements DROP FOREIGN KEY FK_2A6AE999D9A7F869');
        $this->addSql('ALTER TABLE reservations_equipements DROP FOREIGN KEY FK_2A6AE999852CCFF5');
        $this->addSql('ALTER TABLE terrain DROP FOREIGN KEY FK_C87653B1A76ED395');
        $this->addSql('DROP TABLE equipements');
        $this->addSql('DROP TABLE reservations');
        $this->addSql('DROP TABLE reservations_terrain');
        $this->addSql('DROP TABLE reservations_equipements');
        $this->addSql('DROP TABLE terrain');
        $this->addSql('DROP TABLE user');
    }
}
